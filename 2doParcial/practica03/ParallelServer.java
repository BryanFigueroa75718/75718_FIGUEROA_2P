import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelServer {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(6);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor esperando clientes en el puerto 8080...");
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());
                pool.submit(() -> handlerRequest(client));
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
            System.out.println("Se apagó el servidor");
        }
    }

    private static void handlerRequest(Socket client) {
        try (client;
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            client.setSoTimeout(30_000);

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("salir")) {
                    out.println("Adiós, cerrando conexión.");
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " recibió: " + mensaje);
                out.println("Recibido: " + mensaje);
            }
            System.out.println(Thread.currentThread().getName() + ": cliente desconectado");

        } catch (SocketTimeoutException e) {
            System.out.println("Cliente desconectado por inactividad");
        } catch (IOException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        }
    }
}