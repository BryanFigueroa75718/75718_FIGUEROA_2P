import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelServer {

    public static void main(String[] args) {
        int nucleos = Runtime.getRuntime().availableProcessors();

        ExecutorService pool = Executors.newFixedThreadPool(nucleos);
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor escuchando en el puerto 8080...");
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

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String msj = in.readLine();

            while (msj != null) {
                System.out.println("Mensaje: " + msj);

                if (msj.equals("salir")) {
                    out.println("Adiós, cerrando conexión");
                    break;
                }

                out.println("Recibido: " + msj);
                msj = in.readLine();
            }

            System.out.println("Cliente desconectado.");

        } catch (IOException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        }
    }
}