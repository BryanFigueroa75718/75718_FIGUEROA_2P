// Erbet Gomez Bohorquez
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CurrentClient {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        try (
                Socket socket = new Socket(host, puerto);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in) // Para leer desde la consola
        ) {
            System.out.println("Conectado al servidor en " + host + ":" + puerto);
            System.out.println("Escribe tus mensajes (escribe 'salir' para terminar):");

            while (true) {
                String mensaje = scanner.nextLine();
                out.println(mensaje); // Enviamos el mensaje al servidor

                // Mecanismo de salida limpia
                if ("salir".equalsIgnoreCase(mensaje)) {
                    System.out.println("Cerrando la conexión...");
                    break; // Rompe el bucle y permite que el try-with-resources cierre todo
                }
                
                System.out.println("Mensaje enviado con éxito.");
            }

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}