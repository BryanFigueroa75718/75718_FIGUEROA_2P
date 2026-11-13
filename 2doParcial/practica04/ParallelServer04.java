import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelServer04 {

    private static final List<PrintWriter> clientes = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();

        new Thread(() -> {
            Scanner teclado = new Scanner(System.in);
            while (teclado.hasNextLine()) {
                enviarATodos("[SERVIDOR]: " + teclado.nextLine());
            }
        }).start();

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor esperando clientes en el puerto 8080...");
            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(() -> handlerRequest(client));
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void enviarATodos(String mensaje) {
        for (PrintWriter c : clientes) {
            c.println(mensaje);
        }
    }

    private static void handlerRequest(Socket client) {
        PrintWriter out = null;
        String nombre = "?";
        try (client;
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            out = new PrintWriter(client.getOutputStream(), true);

            nombre = in.readLine();
            clientes.add(out);
            enviarATodos(nombre + " se unió al chat");

            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("salir")) break;
                enviarATodos(nombre + ": " + mensaje);
            }

        } catch (IOException e) {
            System.out.println("Error con cliente: " + e.getMessage());
        } finally {
            if (out != null) {
                clientes.remove(out);
                enviarATodos(nombre + " salió del chat");
            }
        }
    }
}