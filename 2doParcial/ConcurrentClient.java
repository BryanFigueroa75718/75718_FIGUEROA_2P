import java.io.*;
import java.net.*;

public class ConcurrentClient {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        try (Socket socket = new Socket(host, puerto);
             BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {

            System.out.println("Conexión exitosa: " + host + ":" + puerto);

            // ---- Login: se repite hasta que el servidor acepte el nombre ----
            String nombre = null;
            while (nombre == null) {
                System.out.print("Nombre de usuario: ");
                String intento = teclado.readLine();
                if (intento == null) {
                    return; // se cerró la entrada (Ctrl+D / Ctrl+Z)
                }

                output.write(intento);
                output.newLine();
                output.flush();

                String respuesta = input.readLine();
                if (respuesta == null) {
                    System.out.println("El servidor cerró la conexión");
                    return;
                }
                if (respuesta.equals("OK")) {
                    nombre = intento.trim();
                } else if (respuesta.startsWith("ERROR:")) {
                    System.out.println(respuesta.substring("ERROR:".length()));
                }
            }

            System.out.println("[" + nombre + "] Conectado. Escribe 'salir' para terminar.");

            // Hilo receptor: imprime lo que llegue del servidor sin bloquear el teclado
            Thread receptor = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = input.readLine()) != null) {
                        System.out.print("\r" + linea + "\n> ");
                    }
                    System.out.println("\rEl servidor cerró la conexión");
                } catch (IOException e) {
                    // Ocurre al cerrar el socket al salir; no es un error real
                }
            });
            receptor.setDaemon(true);
            receptor.start();

            System.out.print("> ");
            String mensaje;
            while ((mensaje = teclado.readLine()) != null) {
                if (mensaje.isBlank()) {
                    System.out.print("> ");
                    continue;
                }

                // Ya no se antepone el nombre: el servidor sabe quién es este cliente
                output.write(mensaje);
                output.newLine();
                output.flush();

                if (mensaje.equalsIgnoreCase("salir")) {
                    receptor.join(1000);
                    break;
                }
                System.out.print("> ");
            }

            System.out.println("[" + nombre + "] Sesión terminada.");

        } catch (SocketException e) {
            System.out.println("Conexión perdida: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}