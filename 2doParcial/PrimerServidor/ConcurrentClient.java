import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConcurrentClient {
    public static void main(String[] args) {

        String host = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(host, port);
             Scanner teclado = new Scanner(System.in)) {
            System.out.println("Conexion exitosa: " + host + ":" + port);
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String mensaje;
            while (true) {
                System.out.print("Mensaje (escribe 'Exit' para terminar): ");
                mensaje = teclado.nextLine();
                salida.println(mensaje);

                String respuesta = entrada.readLine();
                System.out.println("Servidor: " + respuesta);

                if (mensaje.equalsIgnoreCase("Exit")) {
                    break;
                }
            }
            System.out.println("Conexion cerrada.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}