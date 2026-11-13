import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelServer {

    private static final int SERVER_PORT = 8080;
    private static final AtomicInteger connectedClientsCount = new AtomicInteger(0);
    private static final ConcurrentHashMap<String, PrintWriter> activeUserSessions = new ConcurrentHashMap<>();

    private static void broadcastPayloadToAll(String messagePayload, String excludedPeer) {
        for (Map.Entry<String, PrintWriter> sessionEntry : activeUserSessions.entrySet()) {
            if (excludedPeer == null || !sessionEntry.getKey().equals(excludedPeer)) {
                sessionEntry.getValue().println(messagePayload);
            }
        }
    }

    private static void monitorConsoleInput() {
        try (BufferedReader consoleInputReader = new BufferedReader(new InputStreamReader(System.in))) {
            String consoleLine;
            while ((consoleLine = consoleInputReader.readLine()) != null) {
                if (!consoleLine.trim().isEmpty()) {
                    String formattedServerAnnouncement = "[SERVIDOR]: " + consoleLine;
                    broadcastPayloadToAll(formattedServerAnnouncement, null);
                }
            }
        } catch (IOException ex) {
            System.err.println("Falla leyendo stream de consola del daemon: " + ex.getMessage());
        }
    }

    private static void processPeerConnection(Socket remoteSocket) {
        int currentActiveCount = connectedClientsCount.incrementAndGet();
        String peerRemoteAddress = remoteSocket.getRemoteSocketAddress().toString();
        String currentWorkerThread = Thread.currentThread().getName();
        String verifiedUsername = null;

        System.out.println("[+] [" + currentWorkerThread + "] Solicitud entrante desde " + peerRemoteAddress
                + " | Conexiones activas: " + currentActiveCount);

        try (
            Socket clientSocketInstance = remoteSocket;
            BufferedReader socketInputStream = new BufferedReader(new InputStreamReader(clientSocketInstance.getInputStream()));
            PrintWriter socketOutputStream = new PrintWriter(clientSocketInstance.getOutputStream(), true)
        ) {
            // --- ETAPA 1: VALIDACIÓN Y REGISTRO ---
            socketOutputStream.println("LOGIN:Ingresa tu nombre de usuario:");
            String candidateNameInput;

            while ((candidateNameInput = socketInputStream.readLine()) != null) {
                candidateNameInput = candidateNameInput.trim();

                if (candidateNameInput.isEmpty() || candidateNameInput.equalsIgnoreCase("SERVIDOR")) {
                    socketOutputStream.println("ERROR:Nombre inválido o reservado. Ingresa otro:");
                    continue;
                }

                boolean successfullyRegistered = false;
                synchronized (activeUserSessions) {
                    String targetAlias = candidateNameInput;
                    boolean aliasAlreadyTaken = activeUserSessions.keySet().stream()
                            .anyMatch(existingUser -> existingUser.equalsIgnoreCase(targetAlias));
                    if (!aliasAlreadyTaken) {
                        activeUserSessions.put(targetAlias, socketOutputStream);
                        verifiedUsername = targetAlias;
                        successfullyRegistered = true;
                    }
                }

                if (successfullyRegistered) {
                    socketOutputStream.println("OK:Bienvenido al chat, " + verifiedUsername + ". Escribe 'salir' para desconectarte.");
                    break;
                } else {
                    socketOutputStream.println("ERROR:El usuario '" + candidateNameInput + "' ya está conectado. Elige otro:");
                }
            }

            if (verifiedUsername == null) {
                return;
            }

            System.out.println("[*] Sesión autenticada: '" + verifiedUsername + "' (" + peerRemoteAddress + ")");
            broadcastPayloadToAll("*** " + verifiedUsername + " se ha unido al chat ***", verifiedUsername);

            // --- ETAPA 2: FLUJO DE MENSAJERÍA ---
            String incomingStreamData;
            while ((incomingStreamData = socketInputStream.readLine()) != null) {
                if ("salir".equalsIgnoreCase(incomingStreamData.trim())) {
                    socketOutputStream.println("Conexión finalizada por el servidor. ¡Hasta luego, " + verifiedUsername + "!");
                    break;
                }

                if (!incomingStreamData.trim().isEmpty()) {
                    String relayedMessage = "[" + verifiedUsername + "]: " + incomingStreamData;
                    System.out.println(relayedMessage);
                    broadcastPayloadToAll(relayedMessage, verifiedUsername);
                }
            }

        } catch (IOException ex) {
            System.out.println("[-] [" + currentWorkerThread + "] Enlace interrumpido con "
                    + (verifiedUsername != null ? verifiedUsername : peerRemoteAddress));
        } finally {
            if (verifiedUsername != null) {
                activeUserSessions.remove(verifiedUsername);
                System.out.println("[-] Usuario '" + verifiedUsername + "' abandonó el canal.");
                broadcastPayloadToAll("*** " + verifiedUsername + " ha salido del chat ***", verifiedUsername);
            }
            int remainingSessions = connectedClientsCount.decrementAndGet();
            System.out.println("[-] [" + currentWorkerThread + "] Socket liberado (" + peerRemoteAddress
                    + ") | Clientes restantes: " + remainingSessions);
        }
    }

    public static void main(String[] startupArguments) {
        int cpuCoresCount = Runtime.getRuntime().availableProcessors();
        ExecutorService taskThreadPool = Executors.newCachedThreadPool();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApagando daemon de red...");
            broadcastPayloadToAll("*** El servidor se está apagando. Conexión cerrada. ***", null);
            taskThreadPool.shutdownNow();
            System.out.println("Daemon apagado correctamente.");
        }));

        System.out.println("==================================================");
        System.out.println(" Servidor de Chat Distribuido activo en puerto " + SERVER_PORT);
        System.out.println(" Cores CPU: " + cpuCoresCount + " | Executor Service Dinámico");
        System.out.println(" Ingresa texto en esta consola para difundir globalmente.");
        System.out.println("==================================================");

        Thread serverConsoleDaemon = new Thread(() -> monitorConsoleInput());
        serverConsoleDaemon.setDaemon(true);
        serverConsoleDaemon.start();

        try (ServerSocket serverSocketListener = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket incomingPeerSocket = serverSocketListener.accept();
                taskThreadPool.submit(() -> processPeerConnection(incomingPeerSocket));
            }
        } catch (IOException ex) {
            System.err.println("Falla crítica en el socket principal del servidor: " + ex.getMessage());
        } finally {
            taskThreadPool.shutdown();
        }
    }
}