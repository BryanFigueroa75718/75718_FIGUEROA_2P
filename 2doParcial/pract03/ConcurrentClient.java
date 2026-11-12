import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConcurrentClient {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        try (Socket socket = new Socket(host, puerto)) {
            System.out.println("Conexión exitosa: " + host + ":" + puerto);

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner teclado = new Scanner(System.in);

            output.println("Victor Adrian Romero Minaya :)");
            System.out.println("Servidor: " + input.readLine());

            String mensaje = "";
            while (!mensaje.equals("salir")) {
                System.out.print("Tú: ");
                mensaje = teclado.nextLine();   // espera a que escribas
                output.println(mensaje);        // lo manda al servidor
                System.out.println("Servidor: " + input.readLine()); // espera la respuesta
            }

            System.out.println("Sesión terminada.");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}