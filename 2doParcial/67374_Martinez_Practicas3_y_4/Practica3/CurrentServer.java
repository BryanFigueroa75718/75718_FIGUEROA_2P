import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CurrentServer {
    public static void main(String[] args) {
        int puerto = 8080;

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) { // Acepta clientes de forma indefinida
                Socket cliente = serverSocket.accept();
                System.out.println("Cliente conectado: " + cliente.getRemoteSocketAddress());

                // Cada cliente se atiende en su propio hilo
                new Thread(() -> atenderCliente(cliente)).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void atenderCliente(Socket cliente) {
        try (
            Socket s = cliente;
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true)
        ) {
            String mensaje;
            while ((mensaje = in.readLine()) != null) {
                System.out.println("Recibido de " + s.getRemoteSocketAddress() + ": " + mensaje);

                // Salida limpia solicitada por el cliente
                if (mensaje.trim().equalsIgnoreCase("salir")) {
                    out.println("ADIOS");
                    System.out.println("Cliente " + s.getRemoteSocketAddress() + " solicitó salir.");
                    break;
                }

                // Responder para confirmar que la sesión sigue activa
                out.println("ECO: " + mensaje);
            }
        } catch (IOException e) {
            System.err.println("Error con el cliente: " + e.getMessage());
        } finally {
            System.out.println("Conexión cerrada con el cliente.");
        }
    }
}