import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class ConcurrentServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Starting server...");
            while (true) {
                Socket client= serverSocket.accept();
                System.out.println((client.getInetAddress().getHostName()));
                new Thread(()-> handleRequest(client)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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