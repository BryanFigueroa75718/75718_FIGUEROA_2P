import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Starting server...");
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + client.getInetAddress().getHostName());
                new Thread(() -> handleRequest(client)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRequest(Socket clientSocket) {
       
        try (clientSocket; 
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String msj;
            
            while ((msj = in.readLine()) != null) {
                // Mecanismo de salida limpia
                if ("salir".equalsIgnoreCase(msj)) {
                    System.out.println("El cliente ha cerrado la sesión limpiamente.");
                    break; 
                }
                System.out.println("Mensaje: " + msj + " listo para ser enviado al cliente.");
            }
        } catch (IOException e) {
            System.err.println("Conexión interrumpida con el cliente.");
        }
        System.out.println("Hilo finalizado. Recursos liberados para este cliente.");
    }
}