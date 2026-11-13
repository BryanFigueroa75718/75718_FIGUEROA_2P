package pract04;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ConcurrentClient {

    public static void main(String[] args) {

        String host = "localhost";
        int puerto = 8080;

        try (
                Socket socket = new Socket(host, puerto);
                PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(), true);
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                Scanner teclado = new Scanner(System.in)
        ) {

            System.out.println("Conectado al servidor.");

            // Recibimos la solicitud del nombre
            System.out.println(entrada.readLine());

            String usuario = teclado.nextLine();

            // Enviamos el nombre al servidor
            salida.println(usuario);

            // Este hilo recibe los mensajes de los demas
            Thread receptor = new Thread(() -> {

                try {
                    String mensaje;

                    while ((mensaje = entrada.readLine()) != null) {
                        System.out.println("\n" + mensaje);
                    }

                } catch (IOException e) {
                    System.out.println("Conexion cerrada.");
                }
            });

            receptor.setDaemon(true);
            receptor.start();

            System.out.println("Ya puedes escribir mensajes.");

            // Seguimos enviando mensajes desde el teclado
            while (teclado.hasNextLine()) {

                String mensaje = teclado.nextLine();

                salida.println(mensaje);

                if (mensaje.equalsIgnoreCase("salir")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Cliente desconectado.");
    }
}

