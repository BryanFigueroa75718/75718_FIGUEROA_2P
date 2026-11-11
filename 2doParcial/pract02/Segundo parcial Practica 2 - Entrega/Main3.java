import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;

public class Main3 {

    // Colores ANSI
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String WHITE = "\033[0;37m";
    public static final String CYAN = "\033[0;36m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RESET = "\033[0m";


    // Longitud permitida: de 3 a 16 caracteres.
    private static final Pattern USER_REGEX =
            Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{2,15}$");

    public static void main(String[] args) {

        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {

            String input;

            System.out.println(CYAN + "================================");
            System.out.println("TERMINAL");
            System.out.println("================================" + RESET);
            System.out.println("Escribe 'help' para ver los comandos.");

            do {

                String systemUser = System.getProperty("user.name", "guest");

                System.out.print(
                        CYAN_BACKGROUND + " " + systemUser
                                + " 📁/~ " + RESET
                                + BLUE_BACKGROUND + " --> " + RESET
                );

                input = br.readLine();

                if (input == null) {
                    break;
                }

                input = input.trim();

                if (input.isEmpty()) {
                    continue;
                }

                fnInput(input, br);

            } while (!input.equalsIgnoreCase("exit()"));

            System.out.println(YELLOW + "Cerrando terminal..." + RESET);

        } catch (IOException e) {
            System.out.println(RED + "Error de entrada/salida: "
                    + e.getMessage() + RESET);
        }
    }

    public static String fnInput(String input, BufferedReader br)
            throws IOException {

        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {

            case "help":
                showHelp();
                break;

            case "login":
                String username;

                if (tokens.length > 1) {
                    username = tokens[1];
                } else {
                    System.out.print("Ingresa tu usuario: ");
                    username = br.readLine();

                    if (username == null) {
                        return "";
                    }

                    username = username.trim();
                }

                login(username);
                break;

            case "exit()":
                // El ciclo principal se encarga de cerrar la terminal.
                break;

            default:
                System.out.println(RED + "Comando no reconocido: "
                        + command + RESET);
                System.out.println("Escribe 'help' para ver los comandos.");
                break;
        }

        return "";
    }

    // Comando HELP
    public static void showHelp() {
        System.out.println(CYAN + "\n=== COMANDOS DISPONIBLES ===" + RESET);

        System.out.println(GREEN + "help" + WHITE
                + "          - Muestra los comandos disponibles.");

        System.out.println(GREEN + "login" + WHITE
                + "         - Solicita un usuario para validarlo.");

        System.out.println(GREEN + "login <user>" + WHITE
                + " - Valida un usuario directamente.");

        System.out.println(GREEN + "exit()" + WHITE
                + "        - Cierra la terminal.");

        System.out.println();
    }

    // Comando LOGIN
    public static void login(String username) {

        if (username == null || username.isEmpty()) {
            System.out.println(RED + "Error: el usuario no puede estar vacío."
                    + RESET);
            return;
        }

        if (USER_REGEX.matcher(username).matches()) {
            System.out.println(GREEN + "Login correcto. Usuario válido: "
                    + username + RESET);
        } else {
            System.out.println(RED + "Login fallido. El usuario no cumple "
                    + "con el formato requerido." + RESET);
            System.out.println(YELLOW
                    + "Debe comenzar con una letra y tener entre 3 y 16 "
                    + "caracteres. Solo se permiten letras, números y '_'."
                    + RESET);
        }
    }
}