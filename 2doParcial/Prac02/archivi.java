import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class archivi {

    // Colores en ANSI
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    public static final String BOLD = "\033[1m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String CYAN_BACKGROUND = "\033[46m";

    // Datos de Alumno y materia
    public static final String MATERIA = "Concurrencia y Paralelismo 5A";
    public static final String ALUMNO = "Diego Valadez Almeyda";

    // Formato del login
    // Reglas del usuario:
    //  ^            -> inicio del texto
    //  [A-Za-z]     -> el primer caracter debe ser una letra
    //  [A-Za-z0-9_] -> los siguientes pueden ser letras, números o guion bajo
    //  {3,15}       -> entre 3 y 15 caracteres de ese tipo (total: 4 a 16)
    //  $            -> fin del texto
    private static final Pattern USUARIO_VALIDO = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{3,15}$");

    // Usuario actual
    private static String usuarioActual = "invitado";

    public static void main (String[] args) {
        inputReader();
    }

    //CLASE 19/10/2026 CONCU Y PARALELISMO
    public static void inputReader() {
        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)
        ) {
            String ch;
            System.out.println(BOLD + CYAN + "#> Prueba comandos como 'printTime' o 'division'. Escribe 'help' para ver todos." + RESET);
            do {
                System.out.println(CYAN_BACKGROUND + WHITE + "@" + usuarioActual + " 📁/~" + RESET);
                System.out.print(GREEN + "--> " + RESET);

                ch = br.readLine();
                if (ch != null && !ch.equalsIgnoreCase("exit()")) {
                    ch = ch.trim();
                    System.out.println(YELLOW + "> comando de longitud = " + ch.length() + RESET);

                    if (ch.equals("help")) {
                        help();
                    } else if (ch.equals("login")) {
                        login(br);
                    } else if (ch.equals("printTime")) {
                        fecha();
                    } else if (ch.equals("division")) {
                        try {
                            System.out.println("Primer número: ");
                            double a = Double.parseDouble(br.readLine());
                            System.out.println("Segundo número: ");
                            double b = Double.parseDouble(br.readLine());
                            division(a, b);
                        } catch (NumberFormatException e) {
                            System.out.println(RED + "Error: debes poner un número válido." + RESET);
                        }
                    } else if (!ch.isEmpty()) {
                        System.out.println(RED + "Comando no reconocido. Escribe 'help'." + RESET);
                    }
                }

            } while (ch != null && !ch.equalsIgnoreCase("exit()"));
            System.out.println(CYAN + "Hasta luego, " + usuarioActual + "." + RESET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Muestra los comandos disponibles junto con la materia y el nombre del alumno
    public static void help() {
        System.out.println(BLUE_BACKGROUND + WHITE + BOLD + " AYUDA " + RESET);
        System.out.println(CYAN + "Materia: " + WHITE + MATERIA + RESET);
        System.out.println(CYAN + "Alumno : " + WHITE + ALUMNO + RESET);
        System.out.println();
        System.out.println(BOLD + "Comandos disponibles:" + RESET);
        System.out.println(GREEN + "  help      " + RESET + "- Muestra esta ayuda");
        System.out.println(GREEN + "  login     " + RESET + "- Inicia sesión (valida el usuario con regex)");
        System.out.println(GREEN + "  printTime " + RESET + "- Muestra la fecha y hora actual");
        System.out.println(GREEN + "  division  " + RESET + "- Divide dos números");
        System.out.println(GREEN + "  exit()    " + RESET + "- Cierra la terminal");
    }

    // Pide un nombre de usuario y lo valida
    public static void login(BufferedReader br) throws IOException {
        System.out.println("Usuario (4-16 caracteres, empieza con letra, solo letras/números/_): ");
        String usuario = br.readLine();

        // Si el usuario cierra la entrada (Ctrl+D / Ctrl+Z), readLine() devuelve null
        if (usuario == null) {
            System.out.println(RED + "Login cancelado." + RESET);
            return;
        }

        usuario = usuario.trim();
        Matcher m = USUARIO_VALIDO.matcher(usuario);

        if (m.matches()) {
            usuarioActual = usuario;
            System.out.println(GREEN + "Login correcto. Bienvenido, " + usuarioActual + "!" + RESET);
        } else {
            System.out.println(RED + "Usuario inválido. No cumple el formato requerido." + RESET);
        }
    }

    public static void fecha() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println(CYAN + "Fecha y hora: " + ahora.format(formato) + RESET);
    }

    public static void division(double num1, double num2) {
        double resultado = num1 / num2;
        System.out.println(GREEN + "\nResultado = " + resultado + RESET);
    }
}