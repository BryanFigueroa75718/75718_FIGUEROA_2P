import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentServer {

    public static void main(String[] args) {
        int puerto = 8080;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado en el puerto " + puerto);
            System.out.println("Esperando clientes");

            while (true) {
                Socket socket = servidor.accept();

                System.out.println("Nuevo cliente: "
                        + socket.getInetAddress());

                Thread hilo = new Thread(() -> atenderCliente(socket));
                hilo.start();
            }

        } catch (IOException e) {
            System.err.println("Error del servidor: " + e.getMessage());
        }
    }

    public static void atenderCliente(Socket socket) {
        try (
                Socket cliente = socket;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(cliente.getInputStream()));
                PrintWriter out = new PrintWriter(
                        cliente.getOutputStream(), true)
        ) {
            String mensaje;

            while ((mensaje = in.readLine()) != null) {
                System.out.println("[" + cliente.getPort()
                        + "] Cliente: " + mensaje);

                if (mensaje.equalsIgnoreCase("exit")) {
                    out.println("Conexión cerrada");
                    System.out.println("Cliente desconectado");
                    break;
                }

                out.println("Mensaje recibido: " + mensaje);
            }

        } catch (IOException e) {
            System.err.println("Error atendiendo cliente: "
                    + e.getMessage());
        }
    }
}