import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelServer {

    public static void main(String[] args) {
        int nucleos = Runtime.getRuntime().availableProcessors(); //12
        
        ExecutorService pool = Executors.newFixedThreadPool(nucleos);
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket client = serverSocket.accept();

                pool.submit( ()->handleRequest(client) ); 
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            pool.shutdown();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);)
        {
            String msj = in.readLine();
            System.out.println("Mensaje: " + msj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}