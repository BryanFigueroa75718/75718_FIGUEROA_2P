import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class ConcurrentServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor escuchando en el puerto 8080...");
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress().getHostName());
                new Thread(() -> handleRequest(client)).start();
            }
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (Socket client = clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String msj;

            while ((msj = in.readLine()) != null) {
                System.out.println("Cliente: " + msj);
                if (msj.equalsIgnoreCase("salir")) {
                    out.println("Hasta luego.");
                    System.out.println("Servidor: Hasta luego.");
                    break;
                }
                out.println("Recibi: " + msj);
                System.out.println("Servidor: Recibi: " + msj);
            }
        } catch (IOException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        }
        System.out.println("Sesion terminada. Conexion cerrada");
    }
}