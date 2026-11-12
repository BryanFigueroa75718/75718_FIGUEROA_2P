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

            System.out.print("Escribe tu nombre: ");
            String nombre = teclado.nextLine();
            output.println(nombre);

            LectorServidor lector = new LectorServidor(input);
            lector.start();

            String mensaje = "";
            while (!mensaje.equals("salir")) {
                mensaje = teclado.nextLine();
                output.println(mensaje);
            }

            System.out.println("Sesión terminada.");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

class LectorServidor extends Thread {
    private BufferedReader input;

    public LectorServidor(BufferedReader input) {
        this.input = input;
    }

    public void run() {
        try {
            String mensaje = input.readLine();
            while (mensaje != null) {
                System.out.println(mensaje);
                mensaje = input.readLine();
            }
        } catch (IOException e) {
        }
    }
}