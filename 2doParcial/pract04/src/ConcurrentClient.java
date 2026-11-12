import java.io.*;
import java.net.*;

public class ConcurrentClient {
    public static void main(String[] args) {

        String host = "localhost";
        int puerto = 8080;

        try (Socket socket = new Socket(host, puerto);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Conectado a " + host + ":" + puerto);
            System.out.println("Escribe mensajes. Para terminar escribe salir.");
            String mensaje;

            new Thread(() -> {
                try {
                    String respuesta;
                    while ((respuesta = entrada.readLine()) != null) {
                        System.out.println(respuesta);
                    }
                } catch (IOException e) {
                }
                System.out.println("Cliente desconectado.");
                System.exit(0);
            }).start();

            while (true) {
                System.out.print("Tu: ");
                mensaje = teclado.readLine();

                salida.println(mensaje);

                if (mensaje.equalsIgnoreCase("salir")) {
                    break;
                }
            }
        } catch (ConnectException e) {
            System.err.println("No hay un servidor disponible " );
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }

        System.out.println("Cliente desconectado.");
    }
}