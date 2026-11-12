/*
 * Práctica 02 - Terminal con Streams (2do Parcial)
 * Materia: Concurrencia y Paralelismo
 * Alumno: César Enrique Uc Aguilar
 * Ingeniería en Tecnología en Software - Universidad Autónoma de Campeche
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;

public class Main {

    // ===== Personalización: colores ANSI =====
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String WHITE = "\033[0;37m";
    public static final String BOLD = "\033[1m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String RESET = "\033[0m";  // Text Reset

    // ===== Regex para validar el usuario =====
    // Inicia con letra, 4 a 16 caracteres: letras, números, guion bajo y punto
    private static final Pattern USER_REGEX =
            Pattern.compile("^[A-Za-z][A-Za-z0-9_.]{3,15}$");

    // Usuario con sesión iniciada
    private static String currentUser = "invitado";

    public static void main(String[] args) {
        printBanner();

        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r);
        ){
            String input;

            do {
                // Prompt personalizado
                System.out.println(CYAN_BACKGROUND + "@" + currentUser + " 📁/~" + RESET);
                System.out.print("--> ");
                input = br.readLine();

                if (input != null && !input.equalsIgnoreCase("exit()")) {
                    String res = fnInput(input);
                    if (!res.isEmpty()) {
                        System.out.println(res);
                    }
                }
            } while (input != null && !input.equalsIgnoreCase("exit()"));

            System.out.println(YELLOW + "Hasta luego, " + currentUser + "." + RESET);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ===== Banner de bienvenida =====
    private static void printBanner() {
        System.out.println(BLUE_BACKGROUND + WHITE + BOLD
                + "  TERMINAL CON STREAMS - Concurrencia y Paralelismo  " + RESET);
        System.out.println(WHITE + "Alumno: César Enrique Uc Aguilar" + RESET);
        System.out.println(YELLOW + "Escribe 'help' para ver los comandos. 'exit()' para salir." + RESET);
        System.out.println();
    }

    // ===== Interpreta el comando y regresa el texto de respuesta =====
    public static String fnInput(String input){
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "":
                return "";
            case "help":
                return cmdHelp();
            case "login":
                return cmdLogin(tokens);
            default:
                return RED + "Comando no reconocido: " + tokens[0]
                        + ". Escribe 'help'." + RESET;
        }
    }

    // ===== Comando 1: help =====
    private static String cmdHelp() {
        return YELLOW + "Comandos disponibles:" + RESET + "\n"
                + "  help            -> Muestra esta ayuda\n"
                + "  login <usuario> -> Inicia sesión (el usuario se valida con regex)\n"
                + "  exit()          -> Cierra la terminal";
    }

    // ===== Comando 2: login <usuario> =====
    private static String cmdLogin(String[] tokens) {
        if (tokens.length != 2) {
            return RED + "Uso: login <usuario>" + RESET;
        }

        String user = tokens[1];

        if (USER_REGEX.matcher(user).matches()) {
            currentUser = user;
            return GREEN + "Login exitoso. Bienvenido, " + user + "!" + RESET;
        }

        return RED + "Usuario inválido." + RESET + "\n"
                + "Debe iniciar con letra, tener de 4 a 16 caracteres y solo usar\n"
                + "letras, números, guion bajo (_) o punto (.).";
    }
}
