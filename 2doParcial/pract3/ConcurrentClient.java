import java.io.*;
import java.net.*;

public class ConcurrentClient {
    private static final String PALABRA_SALIDA = "salir";
    private static volatile boolean saliendo = false;

    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        try (Socket socket = new Socket(host, puerto)) {
            System.out.println("Conexión exitosa: " + host + ":" + puerto);

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            // Hilo lector: imprime lo que responde el servidor hasta que éste cierre (EOF).
            Thread lector = new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = input.readLine()) != null) {
                        System.out.println("Servidor: " + mensaje);
                    }
                } catch (IOException e) {
                    if (!saliendo) {
                        System.out.println("Error leyendo del servidor: " + e.getMessage());
                    }
                }
                if (!saliendo) {
                    // El servidor cayó o cerró por su cuenta: no tiene sentido seguir esperando teclado.
                    System.out.println("El servidor cerró la conexión.");
                    System.exit(0);
                }
            }, "lector-servidor");
            lector.start();

            output.println("Angela Pimienta Diaz");

            System.out.println("Escribe mensajes (escribe '" + PALABRA_SALIDA + "' para terminar):");
            String linea;
            while ((linea = teclado.readLine()) != null) {
                saliendo = linea.equalsIgnoreCase(PALABRA_SALIDA);
                output.println(linea);
                if (saliendo) {
                    break;
                }
            }
            saliendo = true; // también cubre Ctrl+D / fin de entrada

            System.out.println("Cerrando conexión...");
            socket.shutdownOutput();   // avisa al servidor que ya no enviaremos más (EOF)
            lector.join(3000);         // espera la despedida del servidor y el fin del hilo lector
            if (lector.isAlive()) {
                socket.close();        // si el servidor no responde, se fuerza el cierre
            }
            System.out.println("Conexión cerrada.");

        } catch (UnknownHostException e) {
            System.err.println("Host desconocido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
