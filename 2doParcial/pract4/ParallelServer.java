import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelServer {

    private static ArrayList<PrintWriter> clientes = new ArrayList<>();

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

    private static synchronized void agregarCliente(PrintWriter out) {
        clientes.add(out);
    }

    private static synchronized void quitarCliente(PrintWriter out) {
        clientes.remove(out);
    }

    private static synchronized void enviarATodos(String mensaje) {
        for (PrintWriter cliente : clientes) {
            cliente.println(mensaje);
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String nombre = in.readLine();
            System.out.println(nombre + " se conectó.");

            agregarCliente(out);
            enviarATodos(nombre + " se unió al chat");

            String msj = in.readLine();

            while (msj != null) {
                System.out.println(nombre + ": " + msj);

                if (msj.equals("salir")) {
                    out.println("Adiós, " + nombre);
                    break;
                }

                enviarATodos(nombre + ": " + msj);
                msj = in.readLine();
            }

            quitarCliente(out);
            enviarATodos(nombre + " salió del chat");
            System.out.println(nombre + " se desconectó.");

        } catch (IOException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        }
    }
}