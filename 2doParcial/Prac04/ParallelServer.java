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

    private static final int PORT = 8080;
    private static final AtomicInteger clientesConectados = new AtomicInteger(0);
    // Mapa seguro para hilos que asocia cada nombre de usuario con su canal de salida
    private static final ConcurrentHashMap<String, PrintWriter> usuariosConectados = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int nucleos = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newCachedThreadPool();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApagando servidor...");
            broadcast("*** El servidor se está apagando. Conexión cerrada. ***", null);
            pool.shutdownNow();
            System.out.println("Se apagó el servidor.");
        }));

        System.out.println("==================================================");
        System.out.println(" Servidor de Chat Paralelo iniciado en el puerto " + PORT);
        System.out.println(" Núcleos disponibles: " + nucleos + " | Pool de hilos dinámico");
        System.out.println(" Escribe aquí en cualquier momento para enviar mensajes a todos.");
        System.out.println("==================================================");

        // Hilo dedicado para que el servidor envíe mensajes a todos los clientes conectados
        Thread consolaServidor = new Thread(() -> leerConsolaServidor());
        consolaServidor.setDaemon(true);
        consolaServidor.start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(() -> handlerRequest(client));
            }
        } catch (IOException e) {
            System.err.println("Error en el ServerSocket: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

   private static void leerConsolaServidor() {
        try (BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            String linea;
            while ((linea = teclado.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String mensajeServidor = "[SERVIDOR]: " + linea;
                    broadcast(mensajeServidor, null);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo la consola del servidor: " + e.getMessage());
        }
    }

    private static void handlerRequest(Socket client) {
        int activos = clientesConectados.incrementAndGet();
        String clientAddress = client.getRemoteSocketAddress().toString();
        String threadName = Thread.currentThread().getName();
        String nombreUsuario = null;

        System.out.println("[+] [" + threadName + "] Conexión entrante desde " + clientAddress
                + " | Conexiones activas: " + activos);

        try (
            Socket socket = client;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // --- FASE 1: LOGIN ---
            out.println("LOGIN:Ingresa tu nombre de usuario:");
            String intentoNombre;

            while ((intentoNombre = in.readLine()) != null) {
                intentoNombre = intentoNombre.trim();

                if (intentoNombre.isEmpty() || intentoNombre.equalsIgnoreCase("SERVIDOR")) {
                    out.println("ERROR:Nombre inválido o reservado. Ingresa otro:");
                    continue;
                }

                // Registro sincronizado para evitar nombres duplicados (ignorando mayúsculas/minúsculas)
                boolean registrado = false;
                synchronized (usuariosConectados) {
                    String candidato = intentoNombre;
                    boolean existe = usuariosConectados.keySet().stream()
                            .anyMatch(u -> u.equalsIgnoreCase(candidato));
                    if (!existe) {
                        usuariosConectados.put(candidato, out);
                        nombreUsuario = candidato;
                        registrado = true;
                    }
                }

                if (registrado) {
                    out.println("OK:Bienvenido al chat, " + nombreUsuario + ". Escribe 'salir' para desconectarte.");
                    break;
                } else {
                    out.println("ERROR:El usuario '" + intentoNombre + "' ya está conectado. Elige otro:");
                }
            }

            // Si el cliente cerró la ventana antes de completar el login
            if (nombreUsuario == null) {
                return;
            }

            System.out.println("[*] Usuario autenticado: '" + nombreUsuario + "' (" + clientAddress + ")");
            broadcast("*** " + nombreUsuario + " se ha unido al chat ***", nombreUsuario);

            // --- FASE 2: CHAT GRUPAL ---
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                if ("salir".equalsIgnoreCase(mensaje.trim())) {
                    out.println("Conexión finalizada por el servidor. ¡Hasta luego, " + nombreUsuario + "!");
                    break;
                }

                if (!mensaje.trim().isEmpty()) {
                    String mensajeFormateado = "[" + nombreUsuario + "]: " + mensaje;
                    System.out.println(mensajeFormateado);
                    // Reenvía el mensaje a todos los demás clientes conectados
                    broadcast(mensajeFormateado, nombreUsuario);
                }
            }

        } catch (IOException e) {
            System.out.println("[-] [" + threadName + "] Conexión interrumpida con "
                    + (nombreUsuario != null ? nombreUsuario : clientAddress));
        } finally {
            if (nombreUsuario != null) {
                usuariosConectados.remove(nombreUsuario);
                System.out.println("[-] Usuario '" + nombreUsuario + "' salió del chat.");
                broadcast("*** " + nombreUsuario + " ha salido del chat ***", nombreUsuario);
            }
            int restantes = clientesConectados.decrementAndGet();
            System.out.println("[-] [" + threadName + "] Socket cerrado (" + clientAddress
                    + ") | Conexiones restantes: " + restantes);
        }
    }

    // Envía un mensaje a todos los usuarios conectados, excepto al remitente (si se especifica)
    private static void broadcast(String mensaje, String remitenteExcluido) {
        for (Map.Entry<String, PrintWriter> entry : usuariosConectados.entrySet()) {
            if (remitenteExcluido == null || !entry.getKey().equals(remitenteExcluido)) {
                entry.getValue().println(mensaje);
            }
        }
    }
}
