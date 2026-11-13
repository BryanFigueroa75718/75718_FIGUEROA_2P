/* Gladys Candy Chumá Pérez - 70238
* PRÁCTICA 3*/

package pract03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrentServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Iniciando servidor...");

            while (true) {
                Socket client = serverSocket.accept();

                System.out.println("Cliente conectado: "
                        + client.getInetAddress().getHostName());

                new Thread(() -> handleRequest(client)).start();
            }

        } catch (IOException e) {
            System.out.println("Error del servidor: " + e.getMessage());
        }
    }

    private static void handleRequest(Socket clientSocket) {

        try (
                Socket socket = clientSocket;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true)
        ) {

            String msj;

            // Sigue leyendo mientras el cliente mande mensajes
            while ((msj = in.readLine()) != null) {

                System.out.println("Mensaje: " + msj);

                // Si el cliente escribe salir, se termina su conexión
                if (msj.equalsIgnoreCase("salir")) {
                    out.println("Conexion finalizada");
                    break;
                }

                out.println("Mensaje recibido");
            }

            System.out.println("Cliente desconectado");

        } catch (IOException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        }
    }
}

