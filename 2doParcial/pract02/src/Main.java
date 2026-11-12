// Universidad Autonoma de Campeche.
// Concurrencia y Paralelismo.
// Docente: Sergio Andres Noh Puch.
// Alumno: Pedro Alejandro Chavez Rivero.
// Actividad: 2doParcial_Prac.02_Terminal con Streams

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Main {

    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String WHITE = "\033[97m"; 

    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String PURPLE_BACKGROUND = "\033[45m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RESET = "\033[0m";

    public static String currentUser = System.getProperty("user.name", "invitado");
    public static String currentTheme = CYAN_BACKGROUND;
    public static String currentPrompt = "--> ";
    public static String currentTag = "[DIR: /~]";

    private static final Pattern USERNAME_REGEX = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{3,15}$");

    public static void main(String[] args) {
        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {

            String input;
            System.out.println(GREEN + "Terminal iniciada. Escribe 'help' para ver los comandos o 'exit()' para salir." + RESET);

            do {
                System.out.println(currentTheme + WHITE + " @" + currentUser + " " + currentTag + " " + RESET);
                System.out.print(currentPrompt);
                input = br.readLine();

                if (input != null && !input.trim().isEmpty() && !input.equalsIgnoreCase("exit()")) {
                    String res = fnInput(input);
                    System.out.println(res);
                }
            } while (input != null && !input.equalsIgnoreCase("exit()"));

            System.out.println("Cerrando sesion...");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fnInput(String input) {
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "help":
                return YELLOW + "=== (: Lista de Comandos Disponibles :) ===\n" + RESET +
                       "  help                : Muestra este menu de ayuda.\n" +
                       "  login <usuario>     : Cambia de usuario (validado con Regex).\n" +
                       "  theme <estilo>      : Personaliza la terminal (cyan, blue, purple).\n" +
                       "  date                : Muestra la fecha y hora actual del sistema.\n" +
                       "  echo <mensaje>      : Imprime un mensaje en la terminal.\n" +
                       "  exit()              : Cierra la terminal.";

            case "login":
                if (tokens.length < 2) {
                    return RED + "[ERROR] Debes ingresar un nombre de usuario. Ejemplo: login <usuario>" + RESET;
                }
                String newUser = tokens[1];
                if (USERNAME_REGEX.matcher(newUser).matches()) {
                    currentUser = newUser;
                    return GREEN + "[OK] Sesion iniciada correctamente como @" + currentUser + RESET;
                } else {
                    return RED + "[ERROR] Usuario invalido. Debe empezar con una letra, tener entre 4 y 16 caracteres y solo usar letras, numeros o '_'." + RESET;
                }

            case "theme":
                if (tokens.length < 2) {
                    return RED + "[ERROR] Especifica un estilo. Uso: theme <cyan|blue|purple>" + RESET;
                }
                switch (tokens[1].toLowerCase()) {
                    case "blue":
                        currentTheme = BLUE_BACKGROUND;
                        currentPrompt = "==> ";
                        currentTag = "<PATH: /~>";
                        break;
                    case "purple":
                        currentTheme = PURPLE_BACKGROUND;
                        currentPrompt = "##> ";
                        currentTag = "{ROOT: /~}";
                        break;
                    case "cyan":
                        currentTheme = CYAN_BACKGROUND;
                        currentPrompt = "--> ";
                        currentTag = "[DIR: /~]";
                        break;
                    default:
                        return RED + "[ERROR] Estilo no reconocido. Opciones validas: cyan, blue, purple." + RESET;
                }
                return GREEN + "[OK] Estilo de la terminal actualizado a: " + tokens[1].toLowerCase() + RESET;

            case "date":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                return "[INFO] Fecha y hora actual: " + dtf.format(LocalDateTime.now());

            case "echo":
                if (tokens.length < 2) {
                    return "[ECHO] (mensaje vacio)";
                }
                String message = String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length));
                return "[ECHO] " + message;

            default:
                return RED + "[ERROR] Comando '" + command + "' no reconocido. Escribe 'help' para ver las opciones." + RESET;
        }
    }
}