7 import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConcurrentClient {

    public static void main(String[] args) {

        // Dirección del servidor
        String host = "localhost";
        int puerto = 8080;

        try (
                Socket socket = new Socket(host, puerto);

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                BufferedReader teclado = new BufferedReader(
                        new InputStreamReader(System.in))
        ) {

            System.out.println("Conectado al servidor");

            // Solicitar datos de inicio de sesión
            System.out.println("\n===== LOGIN =====");

            System.out.print("Usuario: ");
            String usuario = teclado.readLine();

            System.out.print("Contraseña: ");
            String password = teclado.readLine();

            // Enviar credenciales al servidor
            out.println(usuario);
            out.println(password);

            // Recibir resultado del login
            String respuesta = in.readLine();

            if (!"LOGIN_OK".equals(respuesta)) {

                System.out.println("Usuario o contraseña incorrectos");
                System.out.println("Cerrando conexión...");
                return;
            }

            System.out.println("\nLogin exitoso");
            System.out.println("Bienvenido, " + usuario);

            System.out.println("\n===== CHAT BROADCAST =====");
            System.out.println("Escribe tus mensajes");
            System.out.println("Escribe /salir para desconectarte");
            System.out.println("--------------------------------");

            // Hilo para recibir mensajes del servidor
            Thread receptor = new Thread(() -> {

                try {

                    String mensaje;

                    while ((mensaje = in.readLine()) != null) {
                        System.out.println("\n" + mensaje);
                        System.out.print("Mensaje: ");
                    }

                } catch (IOException e) {
                    System.out.println("\nConexión cerrada");
                }
            });

            receptor.start();

            // Enviar mensajes al servidor
            String mensaje;

            while ((mensaje = teclado.readLine()) != null) {

                out.println(mensaje);

                if (mensaje.equalsIgnoreCase("/salir")) {
                    break;
                }
            }

            System.out.println("Desconectando cliente...");

        } catch (IOException e) {

            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}