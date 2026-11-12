import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrenciaServer {


    private static final Map<String, PrintWriter> clientes = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Starting server...");

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println(client.getInetAddress().getHostName());
                new Thread(() -> handleRequest(client)).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRequest(Socket clientSocket) {
        String usuario = null; // Nombre de este cliente

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {

            while (usuario == null) {
                out.println("Escribe tu usuario:");
                String nombre = in.readLine();
                if (nombre == null) return;   // el cliente se fue
                nombre = nombre.trim();

                if (nombre.isEmpty()) {
                    out.println("El usuario no puede estar vacío.");
                } else if (clientes.putIfAbsent(nombre, out) != null) {
                    out.println("Ese usuario ya está en uso.");
                } else {
                    usuario = nombre; // quedó registrado
                }
            }

            out.println("Login correcto. Bienvenido " + usuario + ". Escribe exit() para salir.");
            broadcast("*** " + usuario + " se unió al chat ***");

            // CHAT
            String msj;
            while ((msj = in.readLine()) != null) {
                if (msj.equalsIgnoreCase("exit()")) break;

                System.out.println("[SERVIDOR]- Recibi de " + usuario + ": " + msj);
                broadcast("[" + usuario + "]: " + msj);
            }
        } catch (IOException e) {
            System.out.println("Conexión perdida con " + usuario);
        } finally {
            if (usuario != null) {
                clientes.remove(usuario);
                broadcast("*** " + usuario + " salió del chat ***");
            }
        }
    }

    // NUEVO: manda un mensaje a todos los conectados
    private static void broadcast(String mensaje) {
        for (PrintWriter out : clientes.values()) {
            out.println(mensaje);
        }
    }
}