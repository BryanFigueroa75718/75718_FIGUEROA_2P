import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.io.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ConcurrentServer {

    private static final int PORT = 8080;
    private static final int TIMEOUT_MS = 30_000;

    private static final Set<ClientHandler> clientes = new CopyOnWriteArraySet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor escuchando en el puerto " + PORT + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress().getHostName());
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void broadcast(String mensaje, ClientHandler remitente) {
        for (ClientHandler c : clientes) {
            if (c != remitente) {
                c.enviar(mensaje);
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private String nombre;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        void enviar(String mensaje) {
            if (out != null) {
                out.println(mensaje);
            }
        }

        @Override
        public void run() {
            try (Socket client = socket;
                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter writer = new PrintWriter(client.getOutputStream(), true)) {

                this.out = writer;

                out.println("Servidor: ¿Cuál es tu nombre?");

                client.setSoTimeout(TIMEOUT_MS);

                while (nombre == null || nombre.isEmpty()) {
                    String respuesta = in.readLine();
                    if (respuesta == null) return;
                    nombre = respuesta.trim();
                    if (nombre.isEmpty()) {
                        out.println("Servidor: El nombre no puede estar vacío. ¿Cuál es tu nombre?");
                    }
                }

                out.println("Servidor: Hola " + nombre + ". Escribe 'salir' para desconectarte.");
                System.out.println("Servidor: " + nombre + " se ha unido.");

                clientes.add(this);
                broadcast("Servidor: " + nombre + " se ha unido al chat.", this);

                String msj;
                while ((msj = in.readLine()) != null) {
                    System.out.println(nombre + ": " + msj);

                    if (msj.equalsIgnoreCase("salir")) {
                        out.println("Servidor: Hasta luego, " + nombre + ".");
                        break;
                    }

                    broadcast(nombre + " " + msj, this);
                }

            } catch (SocketTimeoutException e) {
                if (out != null) {
                    out.println("Servidor: Desconectado por inactividad (30 segundos).");
                }
                System.out.println("Servidor: " + (nombre != null ? nombre : "Un cliente") + " desconectado por inactividad.");
            } catch (IOException e) {
                System.out.println("Error con el cliente " + nombre + ": " + e.getMessage());
            } finally {
                clientes.remove(this);
                if (nombre != null && !nombre.isEmpty()) {
                    broadcast("Servidor: " + nombre + " salió del chat.", this);
                }
                System.out.println("Sesion terminada. Conexion cerrada");
            }
        }
    }
}