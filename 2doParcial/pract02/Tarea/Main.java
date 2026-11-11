import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class Main {

    public static final String RESET = "\033[0m";
    public static final String CYAN_BG = "\033[46m";
    public static final String BLUE_BG = "\033[44m";
    public static final String WHITE = "\033[0;37m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";

    private static final Pattern USER_REGEX = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{2,14}$");

    private static String usuarioActual = System.getProperty("user.name");

    public static void main(String[] args) {
        System.out.println("Terminal iniciada. Escribe 'help' para ver comandos o 'exit()' para salir.\n");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String input;

            while (true) {
                System.out.print(CYAN_BG + WHITE + " @" + usuarioActual + " " + RESET + " /~ ");
                System.out.print(BLUE_BG + WHITE + "-->" + RESET + " ");

                input = br.readLine();

                if (input == null) break;

                input = input.trim();

                if (input.equalsIgnoreCase("exit()")) {
                    System.out.println("Cerrando sesión...");
                    break;
                }

                if (!input.isEmpty()) {
                    procesarComando(input);
                }
            }

        } catch (IOException e) {
            System.out.println(RED + "Error al leer entrada: " + e.getMessage() + RESET);
        }
    }

    public static void procesarComando(String input) {
        String[] partes = input.split("\\s+");
        String cmd = partes[0].toLowerCase();

        switch (cmd) {
            case "help":
                mostrarHelp();
                break;

            case "login":
                if (partes.length < 2) {
                    System.out.println(RED + "Error: Falta el usuario. Uso: login <usuario>" + RESET);
                } else {
                    hacerLogin(partes[1]);
                }
                break;

            default:
                System.out.println(RED + "Comando no encontrado. Usa 'help'." + RESET);
                break;
        }
    }

    private static void mostrarHelp() {
        System.out.println("\nComandos disponibles:");
        System.out.println("  help            - Muestra los comandos disponibles.");
        System.out.println("  login <usuario> - Cambia de usuario (Valida con Regex).");
        System.out.println("  exit()          - Sale del programa.\n");
    }

    private static void hacerLogin(String user) {
        if (USER_REGEX.matcher(user).matches()) {
            usuarioActual = user;
            System.out.println(GREEN + "Usuario actualizado a: " + user + RESET);
        } else {
            System.out.println(RED + "Usuario no válido." + RESET);
            System.out.println("Debe iniciar con letra, usar solo letras/números/_ y medir entre 3 y 15 caracteres.");
        }
    }
}