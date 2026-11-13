import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class main2 {


    public static final String GREEN        = "\033[0;32m";   
    public static final String BRIGHT_GREEN = "\033[1;92m";   
    public static final String DIM_GREEN    = "\033[2;32m";   
    public static final String BLACK_BG     = "\033[40m";     
    public static final String RED          = "\033[0;31m";   
    public static final String RESET        = "\033[0m";      

    private static final String USUARIO    = "root";
    private static final String PASSWORD   = "root";
    private static final int    MAX_INTENTOS = 3;

    // Pide usuario y contraseña; devuelve true si son correctos
    private static boolean login(BufferedReader br) throws IOException {
        System.out.println(BRIGHT_GREEN + "=== INICIO DE SESIÓN ===" + RESET);

        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            System.out.print(GREEN + "Usuario: " + BRIGHT_GREEN);
            String user = br.readLine();
            System.out.print(GREEN + "Password: " + BRIGHT_GREEN);
            String pass = br.readLine();
            System.out.print(RESET);

            if (user == null || pass == null) return false; // Fin de entrada (Ctrl+D)

            if (user.trim().equals(USUARIO) && pass.equals(PASSWORD)) {
                System.out.println(BRIGHT_GREEN + "Acceso concedido. Bienvenido, " + USUARIO + ".\n" + RESET);
                return true;
            }

            int restantes = MAX_INTENTOS - intento;
            System.out.println(RED + "Usuario o contraseña incorrectos."
                    + (restantes > 0 ? " Intentos restantes: " + restantes : "") + RESET);
        }
        return false;
    }

    public static void main(String[] args) {
        if (args.length > 0 && isHelpFlag(args[0])) {
            printHelp();
            return;
        }

        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {

            if (!login(br)) {
                System.out.println(RED + "Acceso denegado. Demasiados intentos fallidos." + RESET);
                return;
            }

            printBanner();

            String input;
            do {
                System.out.println(BLACK_BG + BRIGHT_GREEN + "@" + USUARIO
                        + " 📁/~" + RESET);
                System.out.print(GREEN + "--> " + BRIGHT_GREEN);
                input = br.readLine();
                System.out.print(RESET);

                if (input == null || input.trim().equalsIgnoreCase("exit()")) {
                    System.out.println(GREEN + "Desconectando de la Matrix... 👋" + RESET);
                    break;
                }

                String res = fnInput(input);
                if (!res.isEmpty()) {
                    System.out.println(res);
                }
            } while (true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fnInput(String input) {
        if (input.isBlank()) return "";

        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        if (tokens.length > 1 && isHelpFlag(tokens[1])) {
            return commandHelp(command);
        }

        switch (command) {
            case "-h":
            case "--help":
            case "help":
                printHelp();
                return "";

            case "echo":
                return cmdEcho(tokens);

            case "date":
                return cmdDate();

            default:
                return RED + "Comando no reconocido: '" + tokens[0]
                        + "'. Usa -h o --help para ver los comandos." + RESET;
        }
    }

    private static String cmdEcho(String[] tokens) {
        if (tokens.length < 2) {
            return RED + "Uso: echo <texto>" + RESET;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < tokens.length; i++) {
            sb.append(tokens[i]);
            if (i < tokens.length - 1) sb.append(" ");
        }
        return GREEN + sb + RESET;
    }

    private static String cmdDate() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return GREEN + "Fecha y hora: " + BRIGHT_GREEN + LocalDateTime.now().format(fmt) + RESET;
    }

    // ===== Ayuda =====
    private static boolean isHelpFlag(String s) {
        return s.equals("-h") || s.equals("--help");
    }

    public static void printHelp() {
        System.out.println(BRIGHT_GREEN + "┌──────────────────────────────────────────────┐");
        System.out.println("│            TERMINAL MATRIX - AYUDA            │");
        System.out.println("└──────────────────────────────────────────────┘" + RESET);
        System.out.println(GREEN + "Uso:" + RESET + "  <comando> [argumentos]");
        System.out.println();
        System.out.println(GREEN + "Comandos disponibles:" + RESET);
        System.out.println(BRIGHT_GREEN + "  -h, --help, help  " + DIM_GREEN + "Muestra esta ayuda" + RESET);
        System.out.println(BRIGHT_GREEN + "  echo <texto>      " + DIM_GREEN + "Imprime el texto escrito" + RESET);
        System.out.println(BRIGHT_GREEN + "  date              " + DIM_GREEN + "Muestra la fecha y hora actual" + RESET);
        System.out.println(BRIGHT_GREEN + "  exit()            " + DIM_GREEN + "Sale de la terminal" + RESET);
        System.out.println();
        System.out.println(DIM_GREEN + "Tip: escribe '<comando> -h' para ver la ayuda de un comando." + RESET);
    }

    private static String commandHelp(String command) {
        switch (command) {
            case "echo":
                return GREEN + "echo <texto>  → imprime el texto. Ej: echo hola mundo" + RESET;
            case "date":
                return GREEN + "date  → muestra la fecha y hora actual (dd/MM/yyyy HH:mm:ss)" + RESET;
            case "exit()":
                return GREEN + "exit()  → cierra la terminal" + RESET;
            default:
                return RED + "No hay ayuda para '" + command + "'." + RESET;
        }
    }

    private static void printBanner() {
        System.out.println(BRIGHT_GREEN
                + "  ▓▒░ Wake up, Neo... ░▒▓\n"
                + GREEN + "  Escribe -h o --help para ver los comandos.\n" + RESET);
    }
}
