import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConcurrentClient04 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner teclado = new Scanner(System.in)) {

            // Login
            System.out.print("Tu nombre: ");
            output.println(teclado.nextLine());

            Thread lector = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = input.readLine()) != null) {
                        System.out.println(linea);
                    }
                } catch (Exception e) { }
                System.out.println("Conexión cerrada.");
                System.exit(0);
            });
            lector.setDaemon(true);
            lector.start();

            while (teclado.hasNextLine()) {
                String mensaje = teclado.nextLine();
                output.println(mensaje);
                if (mensaje.equalsIgnoreCase("salir")) break;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}