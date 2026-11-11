
//Pimienta Diaz Angela Alicia 5to ITS Practica#02



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Main {

    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";   
    public static final String WHITE = "\033[0;37m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String RESET = "\033[0m";

    // aqui guardo el usuario cuando hace login (null = invitado)
    static String usuario = null;

    public static void main(String[] args) {
        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r);
        ){
            String input;

            // mensaje de bienvenida (parte de personalizar la terminal)
            System.out.println(CYAN_BACKGROUND + " BIENVENIDO A MI TERMINAL " + RESET);
            System.out.println("Escribe help para ver los comandos");
            System.out.println();

            do {
                // si no hay usuario pongo invitado, si hay uso su nombre
                String nombre = "invitado";
                String color = RED;
                if (usuario != null) {
                    nombre = usuario;
                    color = GREEN;
                }

                System.out.println(color + "@" + nombre + " 📁/~" + RESET);
                System.out.print("--> ");
                input = br.readLine();

                if (input != null && !input.equalsIgnoreCase("exit()")){
                    String res = fnInput(input);
                    if (res != null && res.length() > 0) {
                        System.out.println(res);
                    }
                }
            } while (input != null && !input.equalsIgnoreCase("exit()"));

            System.out.println(RED + "Adios" + RESET);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fnInput(String input){
        String[] tokens = input.trim().split("\\s+");
        String command = tokens[0];

        if (command.equalsIgnoreCase("help")) {
            return help();
        } else if (command.equalsIgnoreCase("login")) {
            // necesito al menos 2 tokens: login y el usuario
            if (tokens.length < 2) {
                return RED + "Uso: login <usuario>" + RESET;
            }
            return login(tokens[1]);
        } else if (command.equalsIgnoreCase("whoami")) {
            if (usuario == null) {
                return "No has iniciado sesion";
            }
            return GREEN + "Usuario actual: " + usuario + RESET;
        } else if (command.equalsIgnoreCase("logout")) {
            if (usuario == null) {
                return RED + "No hay sesion activa" + RESET;
            }
            usuario = null;
            return GREEN + "Sesion cerrada" + RESET;
        } else {
            return RED + "Comando no reconocido: " + command + RESET;
        }
    }

    public static String help() {
        String texto = "";
        texto = texto + "Comandos disponibles:\n";
        texto = texto + "  help             Muestra esta ayuda\n";
        texto = texto + "  login <usuario>  Inicia sesion\n";
        texto = texto + "  whoami           Muestra el usuario actual\n";
        texto = texto + "  logout           Cierra sesion\n";
        texto = texto + "  exit()           Salir\n";
        return texto;
    }

    public static String login(String user) {
        // regex sencilla: empieza con letra, de 3 a 16 caracteres,
        // solo letras, numeros o guion bajo
        if (user.matches("^[A-Za-z][A-Za-z0-9_]{2,15}$")) {
            usuario = user;
            return GREEN + "Bienvenido " + user + RESET;
        } else {
            return RED + "Usuario invalido. Debe empezar con letra, "
                    + "tener 3 a 16 caracteres y solo letras/numeros/_" + RESET;
        }
    }
}