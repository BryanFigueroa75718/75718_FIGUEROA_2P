//Erbet Gomez Bohorquez
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;

public class Main {

    // Códigos ANSI para personalizar colores en la terminal
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String WHITE = "\033[0;37m";
    public static final String BLUE = "\033[0;34m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";

    // Variable global para mantener el usuario actual
    private static String currentUser = System.getProperty("user.name");

    public static void main(String[] args) {
        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {
            
            String input;
            System.out.println(BOLD + BLUE + "¡Bienvenido! Escribe 'help' para comenzar." + RESET);

            do {
                // Personalización de la terminal
                System.out.print(BOLD + GREEN + "╭─" + currentUser + "@AppdeMensajeria 📁/~\n" + RESET);
                System.out.print(BOLD + GREEN + "╰─➤ " + RESET);
                
                input = br.readLine();

                if (input != null && !input.trim().isEmpty()) {
                    String res = fnInput(input);
                    if (!res.isEmpty()) {
                        System.out.println(res);
                    }
                }
            } while (input != null && !input.equalsIgnoreCase("exit"));

            System.out.println(YELLOW + "Saliendo de la terminal..." + RESET);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fnInput(String input) {
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        switch (command) {
            case "help":
                return YELLOW + "Comandos disponibles:\n" + RESET +
                       "  " + GREEN + "help" + RESET + "           - Muestra esta lista de comandos.\n" +
                       "  " + GREEN + "login <user>" + RESET + "   - Inicia sesión. Formatos:\n" +
                       "                     * Alumno: al0 + 5 números + @uacam.mx (Ej: al012345@uacam.mx)\n" +
                       "                     * Maestro: 8 letras + @uacam.mx (Ej: juanperz@uacam.mx)\n" +
                       "  " + GREEN + "exit" + RESET + "         - Cierra la terminal.";
            
            case "login":
                if (tokens.length < 2) {
                    return RED + "Error: Faltan argumentos. Uso: login <usuario>" + RESET;
                }
                String newUser = tokens[1];
                
                // Expresiones regulares
                // ^al0\d{5}@uacam.mx.mx$ -> Empieza con al0, seguido de 5 dígitos, y termina en @uacam.mx
                String studianteRegex = "^al0\\d{5}@uacam\\.mx$";
                
                // ^[a-zA-Z]{8}@uacam\.mx$ -> Empieza con exactamente 8 letras, y termina en @uacam.mx
                String MaestroRegex = "^[a-zA-Z]{8}@uacam\\.mx$";
                
                if (Pattern.matches(studianteRegex, newUser)) {
                    // Si queremos que en la terminal solo se vea la parte antes del @, podemos usar split:
                    // currentUser = newUser.split("@")[0]; 
                    currentUser = newUser; // Guardamos el correo completo
                    return GREEN + "¡Inicio de sesión exitoso! Bienvenido alumno: " + currentUser + RESET;
                    
                } else if (Pattern.matches(MaestroRegex, newUser)) {
                    currentUser = newUser;
                    return GREEN + "¡Inicio de sesión exitoso! Bienvenido maestro: " + currentUser + RESET;
                    
                } else {
                    return RED + "Error: Formato de usuario inválido.\n" + 
                           " - Alumno: al0 + 5 números + @uacam.mx (Ej: al012345@uacam.mx)\n" +
                           " - Maestro: 8 letras + @uacam.mx (Ej: juanperz@uacam.mx)" + RESET;
                }

            case "exit":
                return ""; 

            default:
                return RED + "Comando no reconocido: '" + command + "'. Escribe 'help' para ver las opciones." + RESET;
        }
    }
}