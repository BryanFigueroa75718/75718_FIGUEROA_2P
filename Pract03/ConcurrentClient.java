import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ConcurrentClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equalsIgnoreCase("multi") || args[0].matches("\\d+"))) {
            int cantidad = 3;
            if (args[0].matches("\\d+")) {
                cantidad = Integer.parseInt(args[0]);
            } else if (args.length > 1 && args[1].matches("\\d+")) {
                cantidad = Integer.parseInt(args[1]);
            }
            ejecutarMultiplesClientes(cantidad);
        } else {
            String nombreCliente = (args.length > 0) ? args[0] : "Cliente";
            ejecutarClienteInteractivo(nombreCliente);
        }
    }

    private static void ejecutarClienteInteractivo(String nombre) {
        System.out.println("==============================================");
        System.out.println(" Conectando [" + nombre + "] a " + HOST + ":" + PORT + "...");
        System.out.println("==============================================");

        try (
            Socket socket = new Socket(HOST, PORT);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conexion establecida!");
            System.out.println("Puedes mandar varios mensajes en esta misma sesion.");
            System.out.println("Para terminar escribe: salir\n");

            while (true) {
                System.out.print("[" + nombre + "] Tu: ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String mensaje = scanner.nextLine();

                if (mensaje.trim().isEmpty()) {
                    continue;
                }

                output.println("[" + nombre + "] " + mensaje);

                String respuesta = input.readLine();
                if (respuesta == null) {
                    System.out.println("[El servidor cerro la conexion]");
                    break;
                }
                System.out.println("Servidor: " + respuesta);

                if ("salir".equalsIgnoreCase(mensaje.trim())) {
                    System.out.println("\nCerrando sesion...");
                    break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Error: host desconocido (" + HOST + ")");
        } catch (IOException e) {
            System.err.println("Error de comunicacion: " + e.getMessage());
        }

        System.out.println("Conexion finalizada. Socket cerrado.");
    }

    private static void ejecutarMultiplesClientes(int cantidad) {
        System.out.println("==============================================");
        System.out.println(" Iniciando " + cantidad + " clientes al mismo tiempo...");
        System.out.println(" Cada uno mantiene su sesion y manda varios mensajes.");
        System.out.println("==============================================\n");

        Thread[] hilos = new Thread[cantidad];

        for (int i = 0; i < cantidad; i++) {
            final int id = i + 1;
            hilos[i] = new Thread(() -> sesionClienteSimulada(id), "Hilo-Cliente-" + id);
            hilos[i].start();
        }

        for (Thread t : hilos) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n==============================================");
        System.out.println(" Todos los clientes terminaron sus sesiones.");
        System.out.println("==============================================");
    }

    private static void sesionClienteSimulada(int id) {
        String nombre = "Cliente-" + id;
        try (
            Socket socket = new Socket(HOST, PORT);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("[+] " + nombre + " conectado al servidor.");

            enviarYRecibir(output, input, nombre, "Hola servidor, soy " + nombre + " soy mauricio");
            Thread.sleep(1200);

            enviarYRecibir(output, input, nombre, "Segundo mensaje de " + nombre + " en la misma sesion activa");
            Thread.sleep(1200);

            enviarYRecibir(output, input, nombre, "Tercer mensaje consecutivo enviado por " + nombre);
            Thread.sleep(1200);

            enviarYRecibir(output, input, nombre, "salir");

        } catch (UnknownHostException e) {
            System.err.println("[" + nombre + "] Host desconocido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[" + nombre + "] Error E/S: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("[-] " + nombre + " desconectado.");
        }
    }

    private static void enviarYRecibir(PrintWriter out, BufferedReader in, String nombre, String msj) throws IOException {
        out.println(msj);
        System.out.println("   [" + nombre + " -> Servidor] " + msj);
        String respuesta = in.readLine();
        System.out.println("   [" + nombre + " <- Servidor] " + respuesta);
    }
}
