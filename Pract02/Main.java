import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class Main {

    static final String RESET  = "\033[0m";
    static final String BOLD   = "\033[1m";
    static final String DIM    = "\033[2m";
    static final String RED    = "\033[0;31m";
    static final String GREEN  = "\033[0;32m";
    static final String YELLOW = "\033[0;33m";
    static final String CYAN   = "\033[0;36m";
    static final String GRAY   = "\033[0;90m";

    // Validacion de usuario
    static final Pattern USUARIO = Pattern.compile("^[A-Za-z][A-Za-z0-9_.]{2,15}$");

    // Estado de la sesion
    static String usuarioActual = null;

    public static void main(String[] args) {
        banner();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String linea;

            while (true) {
                System.out.print(prompt());
                linea = br.readLine();

                if (linea == null) break;               // Ctrl+D / fin de entrada
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                if (linea.equalsIgnoreCase("exit") || linea.equalsIgnoreCase("exit()")) {
                    System.out.println(GRAY + "Sesion cerrada. Hasta luego." + RESET);
                    break;
                }

                String salida = ejecutar(linea);
                if (!salida.isEmpty()) System.out.println(salida);
            }
        } catch (IOException e) {
            System.err.println(RED + "Error de entrada/salida: " + e.getMessage() + RESET);
        }
    }

    // Interfaz

    static void banner() {
        System.out.println();
        System.out.println(CYAN + BOLD + "  ┌──────────────────────────────┐" + RESET);
        System.out.println(CYAN + BOLD + "  │     Terminal con Streams     │" + RESET);
        System.out.println(CYAN + BOLD + "  └──────────────────────────────┘" + RESET);
        System.out.println(GRAY + "  Escribe " + RESET + YELLOW + "help" + RESET + GRAY
                + " para ver los comandos disponibles." + RESET);
        System.out.println();
    }

    static String prompt() {
        String nombre = (usuarioActual == null) ? "invitado" : usuarioActual;
        String colorNombre = (usuarioActual == null) ? YELLOW : GREEN;
        String host = System.getProperty("os.name", "pc").split("\\s+")[0].toLowerCase();
        return GRAY + "[" + RESET + colorNombre + nombre + RESET
                + GRAY + "@" + host + " ~]" + RESET + CYAN + "$ " + RESET;
    }


    static String ejecutar(String entrada) {
        String[] partes = entrada.split("\\s+");
        String comando = partes[0].toLowerCase();

        switch (comando) {
            case "help":
                return cmdHelp();
            case "login":
                return cmdLogin(partes);
            default:
                return RED + "comando no reconocido: " + RESET + partes[0]
                        + GRAY + "  (prueba con 'help')" + RESET;
        }
    }

    // Comando help

    static String cmdHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append(BOLD).append("Comandos disponibles").append(RESET).append("\n");
        sb.append(String.format("  %s%-16s%s %s%n", YELLOW, "help", RESET,
                "Muestra esta ayuda"));
        sb.append(String.format("  %s%-16s%s %s%n", YELLOW, "login <usuario>", RESET,
                "Inicia sesion con un nombre de usuario valido"));
        sb.append(String.format("  %s%-16s%s %s", YELLOW, "exit", RESET,
                "Cierra la terminal (tambien: exit())"));
        sb.append("\n\n");
        sb.append(GRAY).append("Reglas de login <usuario>: 3-16 caracteres, empieza con una letra, ")
                .append("solo se permiten letras, numeros, '_' y '.'").append(RESET);
        return sb.toString();
    }

    // Comando login 

    static String cmdLogin(String[] partes) {
        if (partes.length != 2) {
            return RED + "uso: " + RESET + "login <usuario>";
        }

        String candidato = partes[1];

        if (!USUARIO.matcher(candidato).matches()) {
            return RED + "usuario invalido: " + RESET + candidato + "\n"
                    + GRAY + "Debe tener 3-16 caracteres, empieza con una letra y usar solo "
                    + "letras, numeros, '_' o '.'" + RESET;
        }

        if (candidato.equals(usuarioActual)) {
            return YELLOW + "ya tienes la sesion iniciada como " + candidato + RESET;
        }

        usuarioActual = candidato;
        return GREEN + "sesion iniciada. Bienvenido, " + BOLD + candidato + RESET;
    }
}
