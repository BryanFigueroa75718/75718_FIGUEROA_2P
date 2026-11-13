package pract04;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

public class ParallelServer {

    // Guardamos los clientes y sus nombres
    private static final Map<PrintWriter, String> clientes =
            new ConcurrentHashMap<>();

    public static void main(String[] args) {

        int nucleos = Runtime.getRuntime().availableProcessors();

        ExecutorService pool =
                Executors.newFixedThreadPool(nucleos);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            System.out.println("Servidor iniciado...");
            System.out.println("Esperando clientes...");

            // Este hilo permite mandar mensajes desde el servidor
            Thread consola = new Thread(() -> {

                Scanner teclado = new Scanner(System.in);

                while (teclado.hasNextLine()) {
                    String mensaje = teclado.nextLine();

                    if (!mensaje.isBlank()) {
                        enviarATodos("[Servidor]: " + mensaje);
                    }
                }
            });

            consola.setDaemon(true);
            consola.start();

            // El servidor sigue aceptando clientes
            while (true) {

                Socket client = serverSocket.accept();

                System.out.println("Nuevo cliente conectado");

                pool.submit(() -> handleRequest(client));
            }

        } catch (IOException e) {
            System.out.println("Error del servidor: " + e.getMessage());

        } finally {
            pool.shutdown();
            System.out.println("Se apagó el servidor");
        }
    }

    private static void handleRequest(Socket clientSocket) {

        PrintWriter salida = null;
        String usuario = null;

        try (
                Socket socket = clientSocket;
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {

            salida = new PrintWriter(
                    socket.getOutputStream(), true);

            // Primero pedimos el nombre para entrar al chat
            salida.println("Ingresa tu nombre de usuario:");

            usuario = entrada.readLine();

            if (usuario == null || usuario.isBlank()) {
                salida.println("Usuario no valido.");
                return;
            }

            usuario = usuario.trim();

            clientes.put(salida, usuario);

            salida.println("Bienvenido al chat, " + usuario);
            salida.println("Escribe salir para desconectarte.");

            enviarATodos(">> " + usuario + " se unio al chat.");

            System.out.println("Cliente registrado: " + usuario);

            String mensaje;

            // Seguimos leyendo los mensajes del mismo cliente
            while ((mensaje = entrada.readLine()) != null) {

                if (mensaje.equalsIgnoreCase("salir")) {
                    break;
                }

                if (!mensaje.isBlank()) {
                    String texto = usuario + ": " + mensaje;

                    System.out.println(texto);

                    enviarATodos(texto);
                }
            }

        } catch (IOException e) {
            System.out.println("Un cliente se desconecto.");

        } finally {

            if (salida != null) {
                clientes.remove(salida);
            }

            if (usuario != null) {
                enviarATodos(">> " + usuario + " salio del chat.");
            }

            System.out.println("Clientes conectados: "
                    + clientes.size());
        }
    }

    // Mandamos el mismo mensaje a todos los clientes
    private static void enviarATodos(String mensaje) {

        for (PrintWriter cliente : clientes.keySet()) {

            synchronized (cliente) {
                cliente.println(mensaje);
            }
        }
    }
}

