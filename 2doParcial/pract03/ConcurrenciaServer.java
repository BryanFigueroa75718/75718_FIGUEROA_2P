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
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrenciaServer {
    public static void main(String[] args) {
        int puerto = 8080;

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Starting server on port " + puerto + "...");

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress().getHostName() + ":" + client.getPort());
                new Thread(() -> handleRequest(client)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error en el servidor: " + e.getMessage(), e);
        }
    }

    private static void handleRequest(Socket clientSocket) {
        String clienteId = clientSocket.getInetAddress().getHostName() + ":" + clientSocket.getPort();

        try (
            Socket socket = clientSocket;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String msj;

            while ((msj = in.readLine()) != null) {
                if (msj.equalsIgnoreCase("salir")) {
                    System.out.println("Cliente [" + clienteId + "] solicito desconexion.");
                    out.println("Servidor: Conexion cerrada correctamente.");
                    break;
                }

                System.out.println("Mensaje de [" + clienteId + "]: " + msj);
                out.println("Servidor recibio: " + msj);
            }

        } catch (IOException e) {
            System.err.println("Error con cliente [" + clienteId + "]: " + e.getMessage());
        } finally {
            System.out.println("Recursos liberados e hilo finalizado para [" + clienteId + "].");
        }
    }
}