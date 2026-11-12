import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentServer {
    static int puerto = 8080;
    static Map<String, String> usuarios = Map.of(
            "Uri", "101",
            "Erpes", "101",
            "Tetingas", "101"
    );
    static Map<PrintWriter, String> clientes =
            new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado en el puerto " + puerto);
            System.out.println("Esperando clientes");
            while (true) {
                Socket socket = servidor.accept();
                System.out.println("Nueva conexión: " + socket.getInetAddress());
                Thread hilo = new Thread(() -> atenderCliente(socket));
                hilo.start();
            }
        } catch (IOException e) {
            System.out.println("Error del servidor: " + e.getMessage());
        }
    }

    public static void atenderCliente(Socket socket) {

        PrintWriter out = null;
        String usuario = null;

        try (
                Socket cliente = socket;
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)) {
            out = salida;

            String nombre = in.readLine();
            String password = in.readLine();

            if (nombre == null || password == null || !password.equals(usuarios.get(nombre))) {

                out.println("LOGIN_FAIL");
                System.out.println("Inicio de sesión rechazado");
                return;
            }

            usuario = nombre;
            clientes.put(out, usuario);
            out.println("LOGIN_OK");
            System.out.println(usuario + " ha iniciado sesión");
            broadcast("*** " + usuario + " se ha conectado ***");
            String mensaje;

            while ((mensaje = in.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("/salir")) {
                    break;
                }
                System.out.println(usuario + ": " + mensaje);
                broadcast(usuario + ": " + mensaje);
            }
        } catch (IOException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        } finally {
            if (out != null && usuario != null) {
                clientes.remove(out);
                broadcast("*** " + usuario + " se ha desconectado ***");
                System.out.println(usuario + " desconectado");
            }
        }
    }
    public static void broadcast(String mensaje) {
        for (PrintWriter cliente : clientes.keySet()) {
            cliente.println(mensaje);
        }
    }
}