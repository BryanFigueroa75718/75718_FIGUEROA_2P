import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelServer {

    private static final int PORT = 8080;
    // Contador para monitorear en tiempo real cuántos clientes están conectados simultáneamente
    private static final AtomicInteger clientesConectados = new AtomicInteger(0);

    public static void main(String[] args) {
        int nucleos = Runtime.getRuntime().availableProcessors();
        // Usamos un pool dinámico (CachedThreadPool) para soportar múltiples clientes
        // conectados simultáneamente de forma continua sin saturar el límite de núcleos físicos.
        ExecutorService pool = Executors.newCachedThreadPool();

        // Gancho para apagar el pool de hilos de forma segura al detener el servidor
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApagando servidor...");
            pool.shutdown();
            System.out.println("Se apago el servidor");
        }));

        System.out.println("==================================================");
        System.out.println(" Servidor Paralelo iniciado en el puerto " + PORT);
        System.out.println(" Núcleos disponibles: " + nucleos + " | Pool de hilos dinámico para múltiples clientes");
        System.out.println(" Esperando conexiones...");
        System.out.println("==================================================");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket client = serverSocket.accept();
                pool.submit(() -> handlerRequest(client));
            }
        } catch (IOException e) {
            System.err.println("Error en el ServerSocket: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }

    private static void handlerRequest(Socket client) {
        int activos = clientesConectados.incrementAndGet();
        String clientAddress = client.getRemoteSocketAddress().toString();
        String threadName = Thread.currentThread().getName();

        System.out.println("[+] [" + threadName + "] Nuevo cliente conectado: " + clientAddress
                + " | Clientes conectados simultáneamente: " + activos);

        try (
            Socket socket = client;
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensaje;
            // Bucle continuo: escucha y responde mensajes mientras la sesión permanezca abierta
            while ((mensaje = in.readLine()) != null) {
                System.out.println("    [" + threadName + "] Recibido de " + clientAddress + ": " + mensaje);

                // Mecanismo de salida limpia por palabra clave
                if ("salir".equalsIgnoreCase(mensaje.trim())) {
                    out.println("Conexión finalizada por el servidor. ¡Hasta luego!");
                    System.out.println("    [" + threadName + "] Cliente " + clientAddress + " envió 'salir'. Cerrando sesión...");
                    break; // Sale del bucle para permitir el cierre ordenado de recursos
                }

                // Respuesta manteniendo la sesión activa
                out.println("Servidor (" + threadName + ") recibió: " + mensaje);
            }
        } catch (IOException e) {
            System.out.println("    [" + threadName + "] Conexión interrumpida con " + clientAddress + ": " + e.getMessage());
        } finally {
            int restantes = clientesConectados.decrementAndGet();
            System.out.println("[-] [" + threadName + "] Cliente desconectado: " + clientAddress
                    + " | Clientes activos restantes: " + restantes);
        }
    }
}
