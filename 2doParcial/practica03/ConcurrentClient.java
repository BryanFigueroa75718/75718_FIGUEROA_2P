
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ConcurrentClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner teclado = new Scanner(System.in)) {

            System.out.println("Conectado. Escribe mensajes (salir para terminar):");
            while (teclado.hasNextLine()) {
                String mensaje = teclado.nextLine();
                output.println(mensaje);

                String respuesta = input.readLine();
                if (respuesta == null) {
                    System.out.println("El servidor cerró la conexión.");
                    break;
                }
                System.out.println(respuesta);

                if (mensaje.equalsIgnoreCase("salir")) break;
            }
            System.out.println("Conexión cerrada.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}