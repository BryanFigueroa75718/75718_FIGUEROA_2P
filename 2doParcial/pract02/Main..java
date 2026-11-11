import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern; // IMPORTANTE para el regex

public class Main {

    // Variable global para saber quién está logueado
    static String usuarioActual = "invitado";

    public static void main(String[] args) {
        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {

            String input;

            while (true) {
                // PERSONALIZAR TERMINAL: Muestra el usuario actual
                System.out.print("[" + usuarioActual + "] 👨‍💻$ ");
                input = br.readLine();

                if (input == null) break;
                input = input.trim();
                if (input.isEmpty()) continue;

                if (isExitCommand(input)) {
                    System.out.println("Saliendo de la terminal...");
                    break;
                }

                String[] parts = input.split("\\s+", 2);
                String command = parts[0].toLowerCase();
                String argsText = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "help":
                        System.out.println("📌 Comandos disponibles:");
                        System.out.println("  help          - Muestra esta ayuda");
                        System.out.println("  echo <texto>  - Imprime el texto proporcionado");
                        System.out.println("  date          - Muestra la fecha y hora actual");
                        System.out.println("  len <texto>   - Muestra la longitud del texto");
                        System.out.println("  reverse <texto> - Invierte el texto que escribas");
                        System.out.println("  login <correo> - Inicia sesión (verifica con Regex)");
                        System.out.println("  exit / quit   - Cierra el programa");
                        break;

                    case "echo":
                        System.out.println(argsText);
                        break;

                    case "date":
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        System.out.println("🕒 " + dtf.format(LocalDateTime.now()));
                        break;

                    case "len":
                        System.out.println("📏 Longitud: " + argsText.length() + " caracteres");
                        break;

                    case "reverse":
                        if (argsText.isEmpty()) {
                            System.out.println("⚠️ Debes escribir un texto. Ej: reverse hola");
                        } else {
                            String invertido = new StringBuilder(argsText).reverse().toString();
                            System.out.println("🔄 " + invertido);
                        }
                        break;    
                        
                    // NUEVO COMANDO LOGIN CON REGEX
                    case "login":
                        if (argsText.isEmpty()) {
                            System.out.println("⚠️ Debes escribir un correo. Ej: login alumno@uacam.mx");
                            break;
                        }
                        
                        // Regex: Valida un correo institucional simple
                        // Explicación: letras/números + @ + letras + . + letras
                        String regexCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
                        
                        if (Pattern.matches(regexCorreo, argsText)) {
                            usuarioActual = argsText.split("@")[0]; // Toma lo que está antes del @ como nombre
                            System.out.println("✅ Login exitoso. Bienvenido, " + usuarioActual);
                            
                            // Validación extra de docente o estudiante
                            if (argsText.contains("docente")) {
                                System.out.println("👨‍🏫 Rol detectado: Docente");
                            } else {
                                System.out.println("🎓 Rol detectado: Estudiante");
                            }
                        } else {
                            System.out.println("❌ Error: Formato de correo inválido.");
                        }
                        break;

                    default:
                        System.out.println(input + " : CommandNotFoundException");
                        System.out.println("Usa 'help' para ver la lista de comandos disponibles.");
                        break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isExitCommand(String input) {
        String cmd = input.toLowerCase();
        return cmd.equals("exit") || cmd.equals("exit()") || cmd.equals("quit") || cmd.equals("salir");
    }
}