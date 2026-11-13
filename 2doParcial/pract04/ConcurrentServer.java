import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentServer {
    private static Set<PrintWriter> clientWriters = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor iniciado. Esperando conexiones en el puerto 8080...");
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
        PrintWriter out = null;
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String username = in.readLine();
            if (username == null) return;

            clientWriters.add(out);
            broadcast("--- " + username + " se ha unido al chat ---", out);

            String msj;
            while ((msj = in.readLine()) != null) {
                if ("salir".equalsIgnoreCase(msj)) {
                    break; 
                }
                broadcast(username + ": " + msj, out);
            }
            
            clientWriters.remove(out);
            broadcast("--- " + username + " ha salido del chat exitosamente ---", out);
            System.out.println("El cliente " + username + " ha cerrado la sesión exitosamente.");

        } catch (IOException e) {
            System.err.println("Conexión interrumpida con un cliente.");
        } finally {
            if (out != null) clientWriters.remove(out);
            try { clientSocket.close(); } catch (IOException e) {}
        }
    }

    
    private static void broadcast(String message, PrintWriter sender) {
        System.out.println("BROADCAST: " + message);
        for (PrintWriter writer : clientWriters) {
            
            writer.println(message);
        }
    }
}