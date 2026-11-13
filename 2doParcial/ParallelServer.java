import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelServer {

    private static final int PUERTO = 8080;
    private static final int TIMEOUT_INACTIVIDAD_MS = 180_000;

    // Usuarios conectados: nombre (en minúsculas) -> canal de salida.
    // Sirve a la vez como registro de nombres en uso y como lista de destinatarios del chat.
    private static final Map<String, PrintWriter> usuarios = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en el puerto " + PUERTO);
            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(() -> handleRequest(client));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }

    /** Envía el mensaje a todos los usuarios excepto al remitente. */
    private static void broadcast(String mensaje, String claveRemitente) {
        for (Map.Entry<String, PrintWriter> entrada : usuarios.entrySet()) {
            if (!entrada.getKey().equals(claveRemitente)) {
                entrada.getValue().println(mensaje);
            }
        }
    }
    
    private static String login(BufferedReader in, PrintWriter out) throws IOException {
        String linea;
        while ((linea = in.readLine()) != null) {
            String nombre = linea.trim();

            if (!nombre.matches("[\\p{L}\\p{N}_-]{1,20}")) {
                out.println("ERROR:Nombre inválido (1-20 caracteres: letras, números, _ o -).");
                continue;
            }
            // putIfAbsent es atómico: dos clientes no pueden quedarse con el mismo nombre
            if (usuarios.putIfAbsent(nombre.toLowerCase(), out) != null) {
                out.println("ERROR:El nombre '" + nombre + "' ya está en uso.");
                continue;
            }
            out.println("OK");
            return nombre;
        }
        return null;
    }

    private static void handleRequest(Socket clientSocket) {
        String cliente = clientSocket.getRemoteSocketAddress().toString();
        String nombre = null; // null mientras no haya hecho login
        System.out.println("[Servidor] Cliente conectado: " + cliente);

        try (clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            clientSocket.setSoTimeout(TIMEOUT_INACTIVIDAD_MS);

            nombre = login(in, out);
            if (nombre == null) {
                return; // se desconectó sin iniciar sesión
            }
            String clave = nombre.toLowerCase();

            System.out.println("[Servidor] " + cliente + " inició sesión como " + nombre);
            out.println("Bienvenido, " + nombre + ". Usuarios conectados: " + usuarios.size());
            broadcast("*** " + nombre + " se unió al chat ***", clave);

            String contenido;
            while ((contenido = in.readLine()) != null) {
                contenido = contenido.trim();
                if (contenido.isEmpty()) {
                    continue;
                }

                if (contenido.equalsIgnoreCase("salir")) {
                    System.out.println("[Servidor] " + nombre + " solicitó salir.");
                    out.println("Hasta luego, " + nombre + ". Conexión cerrada.");
                    break;
                }

                System.out.println("[Servidor] Mensaje de " + nombre + ": " + contenido);
                broadcast(nombre + ": " + contenido, clave);
            }

        } catch (SocketTimeoutException e) {
            System.out.println("[Servidor] " + cliente + " inactivo " + (TIMEOUT_INACTIVIDAD_MS / 1000)
                    + "s. Cerrando conexión.");
        } catch (IOException e) {
            System.out.println("[Servidor] Error con " + cliente + ": " + e.getMessage());
        } finally {
            if (nombre != null) {
                String clave = nombre.toLowerCase();
                usuarios.remove(clave);
                broadcast("*** " + nombre + " salió del chat ***", clave);
            }
        }

        System.out.println("[Servidor] Cliente desconectado: " + cliente);
    }

}