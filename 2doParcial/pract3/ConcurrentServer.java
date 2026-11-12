import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentServer {
    private static final int PUERTO = 8080;
    private static final String PALABRA_SALIDA = "salir";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Starting server... escuchando en el puerto " + PUERTO);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress().getHostName()
                        + ":" + client.getPort());
                new Thread(() -> handleRequest(client), "cliente-" + client.getPort()).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRequest(Socket clientSocket) {
        String id = Thread.currentThread().getName();
        // try-with-resources: al salir del bloque se cierran in, out y el socket,
        // sin importar si se salió por "salir", por EOF o por una excepción.
        try (clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String msj;
            // readLine() devuelve null cuando el cliente cierra su lado de la conexión.
            while ((msj = in.readLine()) != null) {
                if (msj.equalsIgnoreCase(PALABRA_SALIDA)) {
                    System.out.println("[" + id + "] Cliente pidió salir.");
                    out.println("Hasta luego, cerrando conexión.");
                    break; // sale del ciclo -> se cierran los recursos
                }
                System.out.println("[" + id + "] Mensaje: " + msj);
                out.println("Recibido: " + msj);
            }
        } catch (IOException e) {
            // Desconexión abrupta del cliente: se registra, pero no se tumba el servidor.
            System.err.println("[" + id + "] Error con el cliente: " + e.getMessage());
        }
        System.out.println("[" + id + "] Conexión cerrada y hilo finalizado.");
    }
}
