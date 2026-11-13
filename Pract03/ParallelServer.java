import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelServer {

    private static final int PORT = 8080;
    private static final AtomicInteger clientesConectados = new AtomicInteger(0);

    public static void main(String[] args) {
        int nucleos = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newCachedThreadPool();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApagando servidor...");
            pool.shutdown();
            System.out.println("Servidor apagado");
        }));

        System.out.println("==============================================");
        System.out.println(" Servidor paralelo en el puerto " + PORT);
        System.out.println(" Nucleos disponibles: " + nucleos + " | Pool dinamico de hilos");
        System.out.println(" Esperando conexiones...");
        System.out.println("==============================================");

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

    private static void handlerRequest(Socket client) {
        int activos = clientesConectados.incrementAndGet();
        String clientAddress = client.getRemoteSocketAddress().toString();
        String threadName = Thread.currentThread().getName();

        System.out.println("[+] [" + threadName + "] Cliente conectado: " + clientAddress
                + " | Conectados ahora: " + activos);

        try (
            Socket socket = client;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("    [" + threadName + "] De " + clientAddress + ": " + mensaje);

                if ("salir".equalsIgnoreCase(mensaje.trim())) {
                    out.println("Conexion finalizada por el servidor. Hasta luego!");
                    System.out.println("    [" + threadName + "] " + clientAddress + " mando 'salir', cerrando...");
                    break;
                }

                out.println("Servidor (" + threadName + ") recibio: " + mensaje);
            }
        } catch (IOException e) {
            System.out.println("    [" + threadName + "] Se perdio la conexion con " + clientAddress + ": " + e.getMessage());
        } finally {
            int restantes = clientesConectados.decrementAndGet();
            System.out.println("[-] [" + threadName + "] Cliente desconectado: " + clientAddress
                    + " | Quedan: " + restantes);
        }
    }
}
