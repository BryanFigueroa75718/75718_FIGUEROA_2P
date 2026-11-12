import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CurrentClient {
    public static void main(String[] args) {
        String host = "10.5.84.172";
        int puerto = 8080;

        try (
            Socket socket = new Socket(host, puerto);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado al servidor en " + host + ":" + puerto);
            System.out.println("Escribe mensajes ('salir' para terminar):");

            String mensaje;
            while ((mensaje = teclado.readLine()) != null) {
                out.println(mensaje);
                System.out.println("Enviado: " + mensaje);

                // Salida limpia: avisar al servidor y romper el ciclo
                if (mensaje.trim().equalsIgnoreCase("salir")) {
                    System.out.println("Cerrando conexión...");
                    break;
                }

                // Esperar respuesta del servidor (eco/confirmación)
                String respuesta = in.readLine();
                if (respuesta != null) {
                    System.out.println("Servidor responde: " + respuesta);
                } else {
                    System.out.println("El servidor cerró la conexión.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        System.out.println("Cliente finalizado correctamente.");
    }
}