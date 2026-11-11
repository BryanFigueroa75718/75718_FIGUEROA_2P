import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class terminal {

    // Colores y diseño ANSI personalizados
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String WHITE = "\033[0;37m";
    public static final String CYAN = "\033[0;36m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RESET = "\033[0m";

    // Datos del sistema y materia
    private static final String OWNER_NAME = "Alberto";
    private static final String SUBJECT = "Concurrencia y Paralelismo";

    // VARIABLE DE ESTADO: Almacena el usuario actualmente logueado (inicia como "invitado")
    private static String currentUser = "invitado";

    // EXPRESIÓN REGULAR (REGEX) PARA LA VALIDACIÓN DEL USUARIO:
    // - Debe empezar obligatoriamente con una letra.
    // - Puede contener letras, números o guion bajo (_).
    // - Longitud total: de 4 a 16 caracteres.
    private static final String USER_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{3,15}$";
    private static final Pattern USER_PATTERN = Pattern.compile(USER_REGEX);

    public static void main(String[] args) {
        System.out.println(CYAN_BACKGROUND + WHITE + " === TERMINAL DE " + SUBJECT.toUpperCase() + " === " + RESET);
        System.out.println(GREEN + "Desarrollado por " + OWNER_NAME + ". Escribe 'help' para ver los comandos." + RESET + "\n");

        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {
            
            String input;

            do {
                // PROMPT DINÁMICO: Muestra el usuario que está logueado actualmente
                System.out.print(CYAN_BACKGROUND + WHITE + " @" + currentUser + " 📁/~ " + RESET);
                System.out.print("--> ");
                
                input = br.readLine();

                if (input != null && !input.trim().equalsIgnoreCase("exit()")) {
                    String cleanInput = input.trim();
                    if (!cleanInput.isEmpty()) {
                        fnInput(cleanInput);
                    }
                }
            } while (input != null && !input.trim().equalsIgnoreCase("exit()"));

            System.out.println(YELLOW + "Cerrando terminal..." + RESET);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fnInput(String input) {
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "help":
                mostrarAyuda();
                break;

            case "login":
                if (tokens.length < 2) {
                    System.out.println(RED + "[ERROR] Falta el nombre de usuario. Sintaxis: login <usuario>" + RESET);
                } else {
                    String usuarioIngresado = tokens[1];
                    
                    // Validación con Regex
                    if (validarUsuarioConRegex(usuarioIngresado)) {
                        // ACTUALIZACIÓN DEL USUARIO LOGEADO
                        currentUser = usuarioIngresado;
                        System.out.println(GREEN + "✔ [LOGIN EXITOSO] Sesión iniciada correctamente como: " + currentUser + RESET);
                    } else {
                        System.out.println(RED + "✘ [LOGIN DENEGADO] El usuario '" + usuarioIngresado + "' NO cumple con las reglas." + RESET);
                        System.out.println(YELLOW + "  -> Regla: Debe empezar con letra, tener de 4 a 16 caracteres y solo usar letras, números o '_'." + RESET);
                    }
                }
                break;

            case "date":
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println(GREEN + "📅 Fecha y Hora Actual: " + dtf.format(LocalDateTime.now()) + RESET);
                break;

            case "msg":
                if (tokens.length < 2) {
                    System.out.println(RED + "[ERROR] Falta el texto. Sintaxis: msg <tu mensaje>" + RESET);
                } else {
                    StringBuilder mensaje = new StringBuilder();
                    for (int i = 1; i < tokens.length; i++) {
                        mensaje.append(tokens[i]).append(" ");
                    }
                    System.out.println(GREEN + "💬 [" + currentUser + "]: " + mensaje.toString().trim() + RESET);
                }
                break;

            default:
                System.out.println(RED + "Comando no reconocido. Escribe 'help' para ver los comandos válidos." + RESET);
                break;
        }

        return "";
    }

    // Método de validación con Regex
    public static boolean validarUsuarioConRegex(String username) {
        if (username == null) {
            return false;
        }
        Matcher matcher = USER_PATTERN.matcher(username);
        return matcher.matches();
    }

    // Método de ayuda (Help)
    public static void mostrarAyuda() {
        System.out.println(YELLOW + "\n================ GUÍA DE COMANDOS ================" + RESET);
        System.out.println(WHITE + "Propietario : " + CYAN + OWNER_NAME + RESET);
        System.out.println(WHITE + "Materia     : " + CYAN + SUBJECT + RESET);
        System.out.println(WHITE + "Usuario Act : " + CYAN + currentUser + RESET);
        System.out.println("--------------------------------------------------");
        System.out.println(GREEN + "1. help" + RESET + "          : Muestra este menú de ayuda.");
        System.out.println(GREEN + "2. login <user>" + RESET + "   : Valida por Regex y actualiza el usuario activo.");
        System.out.println(GREEN + "3. date" + RESET + "          : Muestra la fecha y hora actual del sistema.");
        System.out.println(GREEN + "4. msg <texto>" + RESET + "   : Imprime un mensaje usando el usuario actual.");
        System.out.println(GREEN + "5. exit()" + RESET + "        : Cierra la aplicación de la terminal.");
        System.out.println(YELLOW + "==================================================\n" + RESET);
    }
}