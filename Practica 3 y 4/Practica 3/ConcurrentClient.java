import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConcurrentClient {
    public static void main(String[] args) {
        String host = "10.5.83.63";
        int puerto = 8080;

        try (
                Socket socket = new Socket(host, puerto);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                BufferedReader teclado = new BufferedReader(
                        new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado al servidor en " + host + ":" + puerto);
            System.out.println("Ya puedes escribir. Escribe 'exit' para salir");

            String mensaje;

            while (true) {
                System.out.print("Cliente: ");
                mensaje = teclado.readLine();

                if (mensaje == null) {
                    break;
                }

                out.println(mensaje);

                if (mensaje.equalsIgnoreCase("exit")) {
                    System.out.println("Cerrando conexión");
                    break;
                }

                String respuesta = in.readLine();

                if (respuesta != null) {
                    System.out.println("Servidor: " + respuesta);
                } else {
                    System.out.println("Conexión cerrada");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }

        System.out.println("Cliente terminado");
    }
}