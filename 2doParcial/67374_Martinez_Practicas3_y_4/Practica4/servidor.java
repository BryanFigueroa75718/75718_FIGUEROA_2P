import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {

    private static final int PUERTO = 5000;

    // Registro de usuarios conectados (nombre -> socket) para evitar duplicados
    private static final ConcurrentHashMap<String, Socket> usuariosConectados = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor escuchando en el puerto " + PUERTO + "...");

            while (true) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nueva conexión desde " + socketCliente.getInetAddress());
                new Thread(() -> atenderCliente(socketCliente)).start();
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void atenderCliente(Socket socketCliente) {
        BufferedReader entrada = null;
        PrintWriter    salida  = null;
        String nombreUsuario   = null;

        try {
            entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            salida  = new PrintWriter(socketCliente.getOutputStream(), true);

            // ---------- FASE DE LOGIN ----------
            salida.println("BIENVENIDO. Ingresa tu nombre de usuario:");

            String intento;
            int intentos = 0;
            final int MAX_INTENTOS = 3;

            while (intentos < MAX_INTENTOS) {
                intento = entrada.readLine();

                // Cliente cerró la conexión antes de loguearse
                if (intento == null) {
                    System.out.println("Cliente desconectado durante el login.");
                    return;
                }

                intento = intento.trim();

                if (intento.isEmpty()) {
                    salida.println("ERROR: el nombre no puede estar vacío. Intenta de nuevo:");
                    intentos++;
                    continue;
                }

                if (intento.equalsIgnoreCase("salir")) {
                    salida.println("Login cancelado. Hasta luego.");
                    return;
                }

                if (usuariosConectados.containsKey(intento)) {
                    salida.println("ERROR: el usuario '" + intento + "' ya está conectado. Elige otro:");
                    intentos++;
                    continue;
                }

                // Login exitoso
                nombreUsuario = intento;
                usuariosConectados.put(nombreUsuario, socketCliente);
                salida.println("OK: Bienvenido, " + nombreUsuario + ". Ya puedes enviar mensajes.");
                System.out.println("Usuario autenticado: " + nombreUsuario);
                break;
            }

            if (nombreUsuario == null) {
                salida.println("Demasiados intentos fallidos. Cerrando conexión.");
                System.out.println("Login fallido desde " + socketCliente.getInetAddress());
                return;
            }

            // ---------- FASE DE CONVERSACIÓN ----------
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("[" + nombreUsuario + "] " + mensaje);

                if (mensaje.equalsIgnoreCase("salir")) {
                    salida.println("Hasta luego, " + nombreUsuario + ". Conexión cerrada.");
                    System.out.println("El usuario " + nombreUsuario + " solicitó salir.");
                    break;
                }

                salida.println("[Servidor -> " + nombreUsuario + "] Recibido: \""
                        + mensaje + "\" (longitud=" + mensaje.length() + ")");
            }

        } catch (IOException e) {
            System.err.println("Error atendiendo a " + nombreUsuario + ": " + e.getMessage());
        } finally {
            // ---------- LIMPIEZA ORDENADA ----------
            if (nombreUsuario != null) {
                usuariosConectados.remove(nombreUsuario);
                System.out.println("Usuario desconectado: " + nombreUsuario);
            }
            try { if (entrada != null) entrada.close(); } catch (IOException ignored) {}
            try { if (salida != null)  salida.close();  } catch (Exception ignored) {}
            try { if (socketCliente != null && !socketCliente.isClosed()) socketCliente.close(); } catch (IOException ignored) {}
            System.out.println("Hilo de cliente finalizado.");
        }
    }
}