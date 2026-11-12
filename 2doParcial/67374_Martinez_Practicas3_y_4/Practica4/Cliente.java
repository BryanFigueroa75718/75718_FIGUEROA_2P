import java.io.*;
import java.net.*;

public class Cliente {

    private static final String HOST   = "localhost";
    private static final int    PUERTO = 5000;

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader entradaTeclado  = null;
        PrintWriter    salida          = null;
        BufferedReader entradaServidor = null;

        try {
            socket = new Socket(HOST, PUERTO);
            System.out.println("Conectado al servidor " + HOST + ":" + PUERTO);

            entradaTeclado  = new BufferedReader(new InputStreamReader(System.in));
            salida          = new PrintWriter(socket.getOutputStream(), true);
            entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // ---------- FASE DE LOGIN ----------
            String prompt = entradaServidor.readLine();
            if (prompt != null) System.out.println("Servidor> " + prompt);

            boolean logueado = false;
            while (!logueado) {
                System.out.print("Usuario> ");
                String usuario = entradaTeclado.readLine();
                if (usuario == null) return;
                salida.println(usuario);

                String respuesta = entradaServidor.readLine();
                if (respuesta == null) {
                    System.out.println("El servidor cerró la conexión durante el login.");
                    return;
                }
                System.out.println("Servidor> " + respuesta);

                if (respuesta.startsWith("OK:")) {
                    logueado = true;
                } else if (respuesta.toLowerCase().contains("cancelado")
                        || respuesta.toLowerCase().contains("demasiados")) {
                    return; // salir del cliente
                }
                // Si no es OK ni cancelación, el servidor pedirá otro intento
            }

            // ---------- FASE DE CONVERSACIÓN ----------
            System.out.println("Escribe 'salir' para terminar la conexión.\n");
            String mensaje;
            while (true) {
                System.out.print("Cliente> ");
                mensaje = entradaTeclado.readLine();
                if (mensaje == null) break;

                salida.println(mensaje);

                if (mensaje.equalsIgnoreCase("salir")) {
                    System.out.println("Cerrando sesión de forma ordenada...");
                    // Leer la despedida del servidor antes de cerrar
                    String despedida = entradaServidor.readLine();
                    if (despedida != null) System.out.println("Servidor> " + despedida);
                    break;
                }

                String respuesta = entradaServidor.readLine();
                if (respuesta == null) {
                    System.out.println("El servidor cerró la conexión.");
                    break;
                }
                System.out.println("Servidor> " + respuesta);
            }

        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } finally {
            try { if (entradaServidor != null) entradaServidor.close(); } catch (IOException ignored) {}
            try { if (salida != null)          salida.close();          } catch (Exception ignored) {}
            try { if (entradaTeclado != null)  entradaTeclado.close();  } catch (IOException ignored) {}
            try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
            System.out.println("Cliente finalizado.");
        }
    }
}