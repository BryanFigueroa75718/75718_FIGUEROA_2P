//Erbet Gomez Bohorquez
import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentServer {
    // Lista global para guardar los canales de escritura (out) de todos los clientes conectados
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
            
            // 1. Lo primero que envía el cliente al conectarse es su nombre
            String username = in.readLine();
            if (username == null) return;

            // 2. Añadimos a este cliente a la lista de difusión
            clientWriters.add(out);
            broadcast("--- " + username + " se ha unido al chat ---", out);

            String msj;
            // 3. Escucha continua
            while ((msj = in.readLine()) != null) {
                if ("salir".equalsIgnoreCase(msj)) {
                    break; 
                }
                // Retransmitimos el mensaje a todos
                broadcast(username + ": " + msj, out);
            }
            
            // 4. Salida limpia
            clientWriters.remove(out);
            broadcast("--- " + username + " ha salido del chat ---", out);
            System.out.println("El cliente " + username + " ha cerrado la sesión.");

        } catch (IOException e) {
            System.err.println("Conexión interrumpida con un cliente.");
        } finally {
            if (out != null) clientWriters.remove(out);
            try { clientSocket.close(); } catch (IOException e) {}
        }
    }

    // Método para enviar mensaje a todos los clientes (excepto opcionalmente al que lo envió)
    private static void broadcast(String message, PrintWriter sender) {
        System.out.println("BROADCAST: " + message); // Ver en consola del servidor
        for (PrintWriter writer : clientWriters) {
            // Opcional: si no quieres que el que envía reciba su propio mensaje, usa: if (writer != sender)
            writer.println(message);
        }
    }
}