/* Gladys Candy Chumá Pérez - 70238
 * PRÁCTICA 3*/

package pract03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConcurrentClient {

    public static void main(String[] args) {

        String host = "localhost";
        int puerto = 8080;

        try (
                Socket socket = new Socket(host, puerto);
                BufferedReader teclado = new BufferedReader(
                        new InputStreamReader(System.in));
                PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {

            System.out.println("Conexion exitosa: "
                    + host + ":" + puerto);

            String mensaje;

            // Permite escribir varios mensajes sin cerrar la conexion
            while (true) {

                System.out.print("Escribe un mensaje: ");
                mensaje = teclado.readLine();

                if (mensaje == null) {
                    break;
                }

                salida.println(mensaje);

                String respuesta = entrada.readLine();

                if (respuesta == null) {
                    System.out.println("El servidor se desconecto");
                    break;
                }

                System.out.println("Respuesta del servidor: " + respuesta);

                // Termina cuando escribimos salir
                if (mensaje.equalsIgnoreCase("salir")) {
                    break;
                }
            }

            System.out.println("Conexion cerrada");

        } catch (IOException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
    }
}

