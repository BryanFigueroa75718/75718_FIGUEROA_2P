// Universidad Autonoma de Campeche.
// Materia: Concurrencia y Paralelismo.
// Alumno: Pedro Alejandro Chavez Rivero.
// Docente: Sergio Andres Noh Puch
// Actividad: 2doParcial_Practicas03_ServidorConcurrente
// Fecha: 29 de Octubre del 2026

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ConcurrenciaClient {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        try (
            Socket socket = new Socket(host, puerto);
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner consola = new Scanner(System.in)
        ) {
            System.out.println("Conexion exitosa con " + host + ":" + puerto);
            System.out.println("Escribe tus mensajes (escribe 'salir' para terminar):");

            String mensajeUsuario;

            while (true) {
                System.out.print("Cliente > ");
                mensajeUsuario = consola.nextLine();

                salida.println(mensajeUsuario);

                if (mensajeUsuario.equalsIgnoreCase("salir")) {
                    String despedida = entrada.readLine();
                    if (despedida != null) {
                        System.out.println(despedida);
                    }
                    System.out.println("Sesion finalizada.");
                    break;
                }

                String respuestaServidor = entrada.readLine();
                if (respuestaServidor != null) {
                    System.out.println(respuestaServidor);
                } else {
                    System.out.println("El servidor cerro la conexion.");
                    break;
                }
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException("Host desconocido: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Error de E/S: " + e.getMessage(), e);
        }
    }
}