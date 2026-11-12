//Erbet Gomez Bohorquez
import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class ChatClient {
    // Códigos ANSI para personalizar colores
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";

    private static String currentUser = null;
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println(BOLD + BLUE + "¡Bienvenido! Escribe 'help' para comenzar." + RESET);

            // FASE 1: Bucle de Autenticación (Terminal)
            while (currentUser == null) {
                System.out.print(BOLD + GREEN + "╭─Invitado@AppdeMensajeria 📁/~\n╰─➤ " + RESET);
                String input = br.readLine();
                
                if (input != null && !input.trim().isEmpty()) {
                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println(YELLOW + "Saliendo de la terminal..." + RESET);
                        return;
                    }
                    String res = processLoginCommand(input);
                    System.out.println(res);
                }
            }

            // FASE 2: Conexión al Servidor (Chat)
            startChat(br);

        } catch (IOException e) {
            System.err.println("Error de I/O: " + e.getMessage());
        }
    }

    private static void startChat(BufferedReader consola) {
        try (
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println(YELLOW + "Conectando al servidor general..." + RESET);
            
            // Nos presentamos enviando el username como primer mensaje
            out.println(currentUser);

            // HILO PARA LEER: Escucha mensajes del servidor sin bloquear la consola
            Thread listener = new Thread(() -> {
                try {
                    String msjServidor;
                    while ((msjServidor = in.readLine()) != null) {
                        // Limpiamos un poco la línea visualmente y mostramos el mensaje
                        System.out.println("\n" + BLUE + msjServidor + RESET);
                        System.out.print(BOLD + GREEN + "╰─➤ " + RESET); // Reimprime la flecha
                    }
                } catch (IOException e) {
                    System.out.println(RED + "Conexión con el servidor cerrada." + RESET);
                }
            });
            listener.start();

            // HILO PRINCIPAL (Escribir): Espera tus inputs para enviarlos
            System.out.println(GREEN + "¡Conectado! Escribe tus mensajes (o 'salir' para terminar)." + RESET);
            while (true) {
                System.out.print(BOLD + GREEN + "╰─➤ " + RESET);
                String mensaje = consola.readLine();
                
                if (mensaje == null || mensaje.trim().isEmpty()) continue;
                
                out.println(mensaje);
                if ("salir".equalsIgnoreCase(mensaje)) {
                    System.out.println(YELLOW + "Saliendo del chat..." + RESET);
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(RED + "No se pudo conectar al servidor en " + HOST + ":" + PORT + RESET);
        }
    }

    private static String processLoginCommand(String input) {
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "help":
                return YELLOW + "Comandos disponibles:\n" + RESET +
                       "  " + GREEN + "help" + RESET + "           - Muestra esta lista de comandos.\n" +
                       "  " + GREEN + "login <user>" + RESET + "   - Inicia sesión. Formatos:\n" +
                       "                     * Alumno: al0 + 5 números + @uacam.mx\n" +
                       "                     * Maestro: 8 letras + @uacam.mx\n" +
                       "  " + GREEN + "exit" + RESET + "           - Cierra la terminal.";
            
            case "login":
                if (tokens.length < 2) return RED + "Error: Faltan argumentos. Uso: login <usuario>" + RESET;
                
                String newUser = tokens[1];
                String studianteRegex = "^al0\\d{5}@uacam\\.mx$";
                String maestroRegex = "^[a-zA-Z]{8}@uacam\\.mx$";
                
                if (Pattern.matches(studianteRegex, newUser)) {
                    currentUser = newUser.split("@")[0]; // Nos quedamos con el alias corto
                    return GREEN + "¡Inicio de sesión exitoso! Bienvenido alumno: " + currentUser + RESET;
                } else if (Pattern.matches(maestroRegex, newUser)) {
                    currentUser = newUser.split("@")[0];
                    return GREEN + "¡Inicio de sesión exitoso! Bienvenido maestro: " + currentUser + RESET;
                } else {
                    return RED + "Error: Formato de usuario inválido." + RESET;
                }
            default:
                return RED + "Comando no reconocido. Escribe 'help'." + RESET;
        }
    }
}