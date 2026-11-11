import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BOLD = "\u001B[1m";


    private static final String USER_REGEX = "^[a-zA-Z0-9_]{3,12}$";
    private static String usuarioActual = "invitado";

    public static void main(String[] args) {
        mostrarBanner();

        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {

            String input;
            do {
                System.out.print(CYAN + usuarioActual + "@tu-terminal" + RESET + ":" + YELLOW + "~$ " + RESET);
                input = br.readLine();

                if (input == null) break;

                String comando = input.trim();

                if (comando.equalsIgnoreCase("exit()")) {
                    System.out.println(GREEN + "FUGAAAAAA" + RESET);
                    break;
                }

                switch (comando.toLowerCase()) {
                    case "1":
                    case "hora":
                        hora();
                        break;
                    case "2":
                    case "mensaje":
                        mensaje();
                        break;
                    case "help":
                        mostrarHelp();
                        break;
                    case "login":
                        login(br);
                        break;
                    case "":
                        break;
                    default:
                        System.out.println(RED + "No te entiendo. Escribe 'help' para ver la lista de comandos." + RESET + "\n");
                        break;
                }

            } while (true);

        } catch (IOException e) {
            System.err.println(RED + "hubo un error en la terminal: " + e.getMessage() + RESET);
        }
    }

    private static void mostrarBanner() {
        System.out.println(BOLD + CYAN + "========================================" + RESET);
        System.out.println(BOLD + GREEN + "      DEV-TERMINAL v2.0 CUSTOM          " + RESET);
        System.out.println(BOLD + CYAN + "========================================" + RESET);
        System.out.println("Escribe " + YELLOW + "'help'" + RESET + " para ver la lista de comandos disponibles.\n");
    }

    public static void mostrarHelp() {
        System.out.println(BOLD + "\n--- MENÚ DE AYUDA (COMANDOS) ---" + RESET);
        System.out.println(YELLOW + "  1 | hora    " + RESET + "-> Muestra la hora actual.");
        System.out.println(YELLOW + "  2 | mensaje " + RESET + "-> Muestra el mensaje guardado.");
        System.out.println(YELLOW + "  login       " + RESET + "-> Inicia sesión y valida el usuario mediante Regex.");
        System.out.println(YELLOW + "  help        " + RESET + "-> Muestra esta pantalla de ayuda.");
        System.out.println(YELLOW + "  exit()      " + RESET + "-> Sale de la terminal.");
        System.out.println(BOLD + "--------------------------------\n" + RESET);
    }

    public static void login(BufferedReader br) throws IOException {
        System.out.print(YELLOW + "Ingresa tu nombre de usuario: " + RESET);
        String usuario = br.readLine();

        if (usuario != null && Pattern.matches(USER_REGEX, usuario.trim())) {
            usuarioActual = usuario.trim();
            System.out.println(GREEN + "✓ Login exitoso. Sesión iniciada como: " + usuarioActual + RESET + "\n");
        } else {
            System.out.println(RED + "✗ Usuario inválido. Debe tener entre 3 y 12 caracteres (solo letras, números y '_')." + RESET + "\n");
        }
    }

    public static void hora() {
        LocalTime tiempo = LocalTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(GREEN + "> La hora actual es: " + tiempo.format(formato) + RESET + "\n");
    }

    public static void mensaje() {
        System.out.println(GREEN + "> ¿Puedes pasarme la tarea?" + RESET + "\n");
    }
}