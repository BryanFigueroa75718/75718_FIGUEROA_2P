import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class Main {

    public static final String RED = "\033[0;31m";
    public static final String WHITE = "\033[0;37m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String RESET = "\033[0m";  // Text Reset

    public static final String USUARIO_ORIGINAL = System.getProperty("user.name", "invitado");
    public static String usuario = USUARIO_ORIGINAL;
    public static final Pattern USUARIO_VALIDO = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]{2,14}");

    public static void main(String[] args) {
        System.out.println(BLUE_BACKGROUND + WHITE + " Mi terminal " + RESET);
        System.out.println("Escribe help para ver los comandos disponibles.");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String input;

            while (true) {
                System.out.print(CYAN_BACKGROUND + " " + usuario + "@mi-terminal " + RESET + " --> ");
                input = br.readLine();

                if (input == null || input.trim().equalsIgnoreCase("exit()")) {
                    System.out.println("Hasta luego!");
                    break;
                }

                String res = fnInput(input);
                if (!res.isEmpty()) {
                    System.out.println(res);
                }
            }

        } catch (IOException e) {
            System.out.println(RED + "Error al leer la entrada: " + e.getMessage() + RESET);
        }
    }

    public static String fnInput(String input){
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase(java.util.Locale.ROOT);

        switch (command) {
            case "help":
                if (tokens.length != 1) {
                    return "Uso: help";
                }
                return "Comandos disponibles:\n"
                        + "  help           Muestra esta ayuda.\n"
                        + "  login usuario  Valida el nombre y cambia el usuario de la terminal.\n"
                        + "                 Usa de 3 a 15 caracteres: letras, numeros o _.\n"
                        + "                 El primer caracter debe ser una letra.\n"
                        + "  whoami         Muestra el usuario actual.\n"
                        + "  logout         Vuelve al usuario original.\n"
                        + "  exit()         Cierra la terminal.";

            case "login":
                if (tokens.length != 2) {
                    return "Uso: login usuario (ejemplo: login leidy_123)";
                }
                // matches verifica que todo el nombre cumpla con la expresion regular.
                if (!USUARIO_VALIDO.matcher(tokens[1]).matches()) {
                    return RED + "Usuario invalido. Usa de 3 a 15 caracteres, empieza con una letra"
                            + " y utiliza solo letras, numeros o _." + RESET;
                }
                usuario = tokens[1];
                return "Bienvenido, " + usuario + "!";

            case "whoami":
                if (tokens.length != 1) {
                    return "Uso: whoami";
                }
                return "Usuario actual: " + usuario;

            case "logout":
                if (tokens.length != 1) {
                    return "Uso: logout";
                }
                usuario = USUARIO_ORIGINAL;
                return "Ahora estas en el usuario original: " + usuario;

            default:
                return RED + "Comando desconocido. Escribe help para ver la ayuda." + RESET;
        }
    }
}
