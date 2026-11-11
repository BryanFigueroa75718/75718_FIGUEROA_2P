import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket; 

public class ConcurrentServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Starting server ...");
            while(true) {
                Socket client = serverSocket.accept();
                System.out.println((client.getInetAddress().getHostName()));
                new Thread(()-> handleRequest(client)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
private static void handleRequest(Socket clientSocket){
    try (clientSocket;
         BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         PrintWriter salida = new PrintWriter(clientSocket.getOutputStream(), true)) {

        String mensaje;
        while ((mensaje = entrada.readLine()) != null) {
            if (mensaje.equalsIgnoreCase("Exit")) {
                salida.println("Conversación Cerrada...");
                break;
            }
            System.out.println("[" + Thread.currentThread().getName() + "] Recibido: " + mensaje);
            salida.println("Recibido: " + mensaje);
        }
        System.out.println("Cliente desconectado: " + clientSocket.getInetAddress().getHostName());

    } catch (IOException e) {
        System.err.println("Error con el cliente: " + e.getMessage());
    }
}
}