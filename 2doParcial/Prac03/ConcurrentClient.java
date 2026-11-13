import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ConcurrentClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        // Si el usuario pasa "multi" o un número, ejecuta múltiples clientes en hilos simultáneos
        if (args.length > 0 && (args[0].equalsIgnoreCase("multi") || args[0].matches("\\d+"))) {
            int cantidad = 3;
            if (args[0].matches("\\d+")) {
                cantidad = Integer.parseInt(args[0]);
            } else if (args.length > 1 && args[1].matches("\\d+")) {
                cantidad = Integer.parseInt(args[1]);
            }
            ejecutarMultiplesClientes(cantidad);
        } else {
            // Modo interactivo individual: perfecto para abrir en varias terminales independientes
            String nombreCliente = (args.length > 0) ? args[0] : "Cliente";
            ejecutarClienteInteractivo(nombreCliente);
        }
    }

    /**
     * Modo Interactivo: Permite escribir múltiples mensajes en la consola dentro de la misma sesión.
     */
    private static void ejecutarClienteInteractivo(String nombre) {
        System.out.println("==================================================");
        System.out.println(" Conectando [" + nombre + "] al Servidor en " + HOST + ":" + PORT + "...");
        System.out.println("==================================================");

        try (
            Socket socket = new Socket(HOST, PORT);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("¡Conexión establecida exitosamente!");
            System.out.println("Puedes enviar múltiples mensajes de forma continua en esta misma sesión.");
            System.out.println("Para terminar la conexión limpiamente, escribe: salir\n");

            while (true) {
                System.out.print("[" + nombre + "] Tú: ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String mensaje = scanner.nextLine();

                if (mensaje.trim().isEmpty()) {
                    continue;
                }

                // Enviar mensaje al servidor
                output.println("[" + nombre + "] " + mensaje);

                // Recibir y mostrar respuesta del servidor
                String respuesta = input.readLine();
                if (respuesta == null) {
                    System.out.println("[El servidor cerró la conexión]");
                    break;
                }
                System.out.println("Servidor: " + respuesta);

                // Mecanismo de salida limpia
                if ("salir".equalsIgnoreCase(mensaje.trim())) {
                    System.out.println("\nCerrando sesión de forma ordenada...");
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Error: Host desconocido (" + HOST + ")");
        } catch (IOException e) {
            System.err.println("Error de comunicación: " + e.getMessage());
        }

        System.out.println("Conexión finalizada. Socket cerrado.");
    }

    /**
     * Modo Múltiples Clientes Concurrentes:
     * Lanza N clientes en hilos paralelos simultáneos. Cada cliente mantiene su conexión
     * abierta, intercambia varios mensajes consecutivos y se despide con 'salir'.
     */
    private static void ejecutarMultiplesClientes(int cantidad) {
        System.out.println("==================================================");
        System.out.println(" Iniciando " + cantidad + " clientes concurrentes simultáneos...");
        System.out.println(" Cada cliente mantendrá su sesión activa e intercambiará mensajes.");
        System.out.println("==================================================\n");

        Thread[] hilos = new Thread[cantidad];

        for (int i = 0; i < cantidad; i++) {
            final int id = i + 1;
            hilos[i] = new Thread(() -> sesionClienteSimulada(id), "Hilo-Cliente-" + id);
            hilos[i].start();
        }

        // Esperar a que todos los clientes completen sus sesiones
        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n==================================================");
        System.out.println(" Todos los clientes concurrentes han finalizado sus sesiones.");
        System.out.println("==================================================");
    }

    private static void sesionClienteSimulada(int id) {
        String nombre = "Cliente-" + id;
        try (
            Socket socket = new Socket(HOST, PORT);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[+] " + nombre + " conectado al servidor.");

            // Mensaje 1
            enviarYRecibir(output, input, nombre, "Hola servidor, soy " + nombre + " soy benja");
            Thread.sleep(1200); // Mantiene la sesión viva mientras los demás clientes interactúan

            // Mensaje 2
            enviarYRecibir(output, input, nombre, "Segundo mensaje de " + nombre + " en la misma sesión activa");
            Thread.sleep(1200);

            // Mensaje 3
            enviarYRecibir(output, input, nombre, "Tercer mensaje consecutivo enviado por " + nombre);
            Thread.sleep(1200);

            // Mensaje 4 (salida limpia)
            enviarYRecibir(output, input, nombre, "salir");

        } catch (UnknownHostException e) {
            System.err.println("[" + nombre + "] Host desconocido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[" + nombre + "] Error E/S: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("[-] " + nombre + " desconectado limpiamente.");
        }
    }

    private static void enviarYRecibir(PrintWriter out, BufferedReader in, String nombre, String msj) throws IOException {
        out.println(msj);
        System.out.println("   [" + nombre + " -> Servidor] " + msj);
        String respuesta = in.readLine();
        System.out.println("   [" + nombre + " <- Servidor] " + respuesta);
    }
}
