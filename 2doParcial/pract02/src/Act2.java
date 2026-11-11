import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * 2DO_PARCIAL_PRACTICA02
 * Terminal con: comando help, comando login (usuario validado con Regex) y terminal personalizada.
 */
public class Act2 {

    // Personalización de la terminal (colores ANSI)
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RESET = "\033[0m";

    // Regex para validar el usuario: letras, números y guion bajo (3 a 15 caracteres)
    private static final Pattern USER_REGEX = Pattern.compile("^[A-Za-z0-9_]{3,15}$");

    // Usuario actual que se muestra en el prompt
    private static String usuario = "invitado";

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String input;

            System.out.println(GREEN + "Bienvenido a la terminal. Escribe 'help' para ver los comandos." + RESET);

            while (true) {
                // Prompt personalizado
                System.out.println(CYAN_BACKGROUND + "@" + usuario + " 📁/~" + RESET);
                System.out.print("--> ");
                input = br.readLine();

                if (input == null || input.equalsIgnoreCase("exit()")) {
                    break;
                }

                String res = fnInput(input);
                if (!res.isEmpty()) {
                    System.out.println(res);
                }
            }

            System.out.println(GREEN + "Hasta luego, " + usuario + "!" + RESET);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Interpreta el comando escrito por el usuario
    public static String fnInput(String input) {
        if (input.trim().isEmpty()) {
            return "";
        }

        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "help":
                return help();
            case "login":
                return login(tokens);
            default:
                return RED + "Comando no reconocido: '" + command + "'. Escribe 'help'." + RESET;
        }
    }

    // Comando help: lista de todos los comandos
    private static String help() {
        return GREEN + "Comandos disponibles:\n" + RESET
                + "  help             -> Lista de todos los comandos\n"
                + "  login <usuario>  -> Inicia sesión (usuario de 3 a 15 caracteres: letras, números o _)\n"
                + "  exit()           -> Salir de la terminal";
    }

    // Comando login: verifica el usuario con Regex
    private static String login(String[] tokens) {
        if (tokens.length < 2) {
            return RED + "Uso: login <usuario>" + RESET;
        }

        String nuevoUsuario = tokens[1];

        if (!USER_REGEX.matcher(nuevoUsuario).matches()) {
            return RED + "Usuario inválido: '" + nuevoUsuario
                    + "'. Usa de 3 a 15 caracteres (letras, números o _)." + RESET;
        }

        usuario = nuevoUsuario;
        return GREEN + "Sesión iniciada como " + usuario + RESET;
    }
}