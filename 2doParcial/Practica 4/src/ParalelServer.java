import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParalelServer {
    public static void main(String[] args) {
        int nucleos = Runtime.getRuntime().availableProcessors();
        System.out.println(nucleos);


        ExecutorService pool = Executors.newFixedThreadPool(nucleos);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true){
                Socket client = serverSocket.accept();

//                pool.submit(() -> handleRequest(client));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Apagando el server");
            pool.shutdown();
        }

    }
}
