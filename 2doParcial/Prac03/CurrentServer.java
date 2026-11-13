import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CurrentServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Iniciando server....");
            while (true) {
                Socket cliente = serverSocket.accept();
                System.out.println(cliente.getInetAddress().getHostAddress());
                new Thread(() -> handleRequest(cliente)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRequest(Socket clinSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clinSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clinSocket.getOutputStream(), true);
            Socket socket = clinSocket // Ensured client socket closes automatically
        ) {
            String msj = in.readLine();
            System.out.println("mensaje: " + msj + "//");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}