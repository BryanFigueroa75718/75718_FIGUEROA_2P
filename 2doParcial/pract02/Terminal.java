import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/** Terminal con Stream – Autor: Victor Adrian Romero Minaya – 21/10/26
 * - Streams: InputStreamReader + BufferedReader (entrada), PrintStream (salida)
 * - Comandos: help, login, logout, eco, tabla + 2 extra :)
 * - Login validado con regex
 * - Terminal personalizada: banner, colores ANSI y prompt con usuario **/

public class Terminal {

    // Colorcitos
    static final String RESET = "\u001B[0m";
    static final String VERDE = "\u001B[32m";
    static final String CIAN = "\u001B[36m";
    static final String AMARILLO = "\u001B[33m";
    static final String ROJO = "\u001B[31m";

    // Este es el regex para el usuario
    // Empieza con letra, luego letras/números/guion bajo, longitud total 4-16
    static final Pattern USUARIO_REGEX = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{3,15}$");

    static String usuario = null; // null = sin sesión
    static final PrintStream out = System.out;

    public static void main(String[] args) {
        try (InputStreamReader isr = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(isr)) {

            banner();
            String linea;
            while (true) {
                prompt();
                linea = br.readLine();
                if (linea == null) break;          // Ctrl+D / fin del stream
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                if (linea.equalsIgnoreCase("exit()") || linea.equalsIgnoreCase("exit")) {
                    out.println(AMARILLO + "Cerrando terminal... ¡hasta luego!" + RESET);
                    break;
                }
                ejecutar(linea, br);
            }
        } catch (IOException e) {
            out.println(ROJO + "Error de E/S: " + e.getMessage() + RESET);
        }
    }

    // aqui empieza el interprete para comandos
    static void ejecutar(String linea, BufferedReader br) throws IOException {
        String[] partes = linea.split("\\s+", 2);
        String cmd = partes[0];
        String arg = partes.length > 1 ? partes[1] : "";

        switch (cmd) {
            case "help":      help(); break;
            case "login":     login(arg, br); break;
            case "logout":    logout(); break;
            case "printTime": fecha(); break;
            case "division":  division(br); break;
            case "eco":       eco(arg); break;
            case "tabla":     tabla(arg); break;
            default:
                out.println(ROJO + "Comando no reconocido: '" + cmd + "'. Escribe 'help'." + RESET);
        }
    }

    // este es el comando help
    static void help() {
        out.println(CIAN + "=== COMANDOS DISPONIBLES ===" + RESET);
        out.println("  help             -> muestra esta ayuda");
        out.println("  login [usuario]  -> inicia sesión (valida usuario con regex)");
        out.println("  logout           -> cierra la sesión");
        out.println("  printTime        -> muestra fecha y hora actual");
        out.println("  division         -> divide dos números");
        out.println("  eco <texto>      -> repite el texto en mayúsculas y al revés");
        out.println("  tabla <n>        -> tabla de multiplicar del número n");
        out.println("  exit()           -> salir de la terminal");
    }

    // comando login
    static void login(String arg, BufferedReader br) throws IOException {
        if (usuario != null) {
            out.println(AMARILLO + "Ya iniciaste sesión como " + usuario + ". Usa 'logout' primero." + RESET);
            return;
        }
        String nombre = arg;
        if (nombre.isEmpty()) {
            out.print("Usuario: ");
            out.flush();
            nombre = br.readLine();
            if (nombre == null) return;
            nombre = nombre.trim();
        }
        if (USUARIO_REGEX.matcher(nombre).matches()) {
            usuario = nombre;
            out.println(VERDE + "Login correcto. ¡Bienvenido, " + usuario + "!" + RESET);
        } else {
            out.println(ROJO + "Usuario inválido. Debe empezar con letra, tener 4-16 caracteres "
                    + "y solo letras, números o '_'." + RESET);
        }
    }

    static void logout() {
        if (usuario == null) {
            out.println(AMARILLO + "No hay ninguna sesión iniciada." + RESET);
        } else {
            out.println(VERDE + "Sesión de " + usuario + " cerrada." + RESET);
            usuario = null;
        }
    }

    // comando 1: eco
    static void eco(String texto) {
        if (texto.isEmpty()) {
            out.println(AMARILLO + "Uso: eco <texto>" + RESET);
            return;
        }
        String reves = "";
        for (int i = texto.length() - 1; i >= 0; i--) {
            reves += texto.charAt(i);
        }
        out.println("Mayúsculas: " + texto.toUpperCase());
        out.println("Al revés:   " + reves);
    }

    // comando 2: tabla
    static void tabla(String arg) {
        try {
            int n = Integer.parseInt(arg.trim());
            for (int i = 1; i <= 10; i++) {
                out.println(n + " x " + i + " = " + (n * i));
            }
        } catch (NumberFormatException e) {
            out.println(AMARILLO + "Uso: tabla <número entero>" + RESET);
        }
    }

    // comandos extras:)
    static void fecha() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        out.println("Fecha y hora: " + LocalDateTime.now().format(f));
    }

    static void division(BufferedReader br) throws IOException {
        try {
            out.print("Primer número: ");
            out.flush();
            double a = Double.parseDouble(br.readLine());
            out.print("Segundo número: ");
            out.flush();
            double b = Double.parseDouble(br.readLine());
            if (b == 0) {
                out.println(ROJO + "Error: no se puede dividir entre cero." + RESET);
            } else {
                out.println("Resultado = " + (a / b));
            }
        } catch (NumberFormatException | NullPointerException e) {
            out.println(ROJO + "Error: debes poner un número válido." + RESET);
        }
    }

    // tuneo de la terminal
    static void banner() {
        out.println(CIAN + "╔══════════════════════════════════╗");
        out.println("║      MI TERMINAL EN JAVA v1.0    ║");
        out.println("╚══════════════════════════════════╝" + RESET);
        out.println("Escribe 'help' para ver los comandos.\n");
    }

    static void prompt() {
        String quien = (usuario == null) ? "invitado" : usuario;
        String color = (usuario == null) ? AMARILLO : VERDE;
        out.print(color + quien + "@terminal" + RESET + " $ ");
        out.flush();
    }
}
