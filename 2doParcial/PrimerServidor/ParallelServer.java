import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelServer {

    public static void main(String[] args) {

        int nucleos = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(nucleos);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {


            System.out.println("Servidor encendido en el puerto 8080 (" + nucleos + " núcleos).");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el ServerSocket: " + e.getMessage());
                }
            }));

            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(() -> handlerRequest(client));
            }
        } catch (IOException e) {
            System.out.println("Servidor detenido.");
        } finally {
            System.out.println("Se apago el servidor");
            pool.shutdown();
        }
    }

    private static void handlerRequest(Socket client) {
    }
}