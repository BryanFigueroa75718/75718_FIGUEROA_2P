import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static final String CYAN_BACKGROUND = "\u001B[46m\u001B[30m"; 
    public static final String RESET = "\u001B[0m";
    public static void main(String[] args) {
        
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (Exception ignored) {}

        try (Reader r = new InputStreamReader(System.in,"UTF-8");
             BufferedReader br = new BufferedReader(r)) {

            String input;

            while (true) {
            
                String carpetaActual = new java.io.File(System.getProperty("user.dir")).getName();
                String usuario =  System.getProperty("user.name");
                System.out.println(CYAN_BACKGROUND + " @" + usuario + " 📁/" + carpetaActual + " " + RESET);
                System.out.print("$");
            
                input = br.readLine();

                if (input == null) break;

             
                input = input.trim();

                
                if (input.isEmpty()) continue;

                if (isExitCommand(input)) {
                    System.out.println(" Saliendo de la terminal...");
                    break;
                }

              
                String[] parts = input.split("\\s+", 2);
                String command = parts[0].toLowerCase();
                String argsText = parts.length > 1 ? parts[1] : "";

                
                switch (command) {
                    case "help":
                        System.out.println("\n");
                        System.out.println("📌 Comandos disponibles:");
                        System.out.println("  help          - Muestra esta ayuda");
                        System.out.println("  echo <texto>  - Imprime el texto proporcionado");
                        System.out.println("  date          - Muestra la fecha y hora actual");
                        System.out.println("  len <texto>   - Muestra la longitud del texto");
                        System.out.println("  exit / quit   - Cierra el programa");
                        System.out.println("  reverse <texto> - Invierte el texto que escribas");
                        System.out.println("  login <email> - Verifica si un correo es de docente o alumno");
                        System.out.println("  pwd           - Muestra la ruta de la carpeta actual");
                        System.out.println("  whoami        - Muestra el nombre del usuario actual");
                        System.out.println("\n");
                        break;

                    case "echo":
                        System.out.println(argsText);
                        break;

                    case "login":
                        validarCorreo(argsText);
                        break;

                    case "whoami":
                        System.out.println("👤 " + usuario + "\n");
                        break;

                    case "date":
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        System.out.println("🕒 " + dtf.format(LocalDateTime.now()));
                        break;

                    case "len":
                        System.out.println("📏 Longitud: " + argsText.length() + " caracteres");
                        break;

                    case "reverse":
                        if (argsText.isEmpty()) {
                            System.out.println("Debes escribir un texto. Ej: reverse hola");
                        } else {
                            String invertido = new StringBuilder(argsText).reverse().toString();
                            System.out.println(invertido);
                        }
                        break;    
                    
                    case "pwd":
                    System.out.println("📂/" + carpetaActual );
                    break;

                    default:
                     
                        System.out.println(input + ":" +  "CommandNotFoundException\n" + "use help para ver la lista de comandos disponibles" );
                        break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isExitCommand(String input) {
        String cmd = input.toLowerCase();
        return cmd.equals("exit") || cmd.equals("exit()") || cmd.equals("quit") || cmd.equals("salir");
    }

    private static void validarCorreo(String email) {
    if (email.isEmpty()) {
        System.out.println("Debes proporcionar un correo. Ej: correo al075462@uacam.mx");
        return;
    }

    String regexAlumno = "(?i)^al\\d+@uacam\\.mx$";

    String regexDocente = "(?i)^(?!al\\d+@)[a-z._%+-]+@uacam\\.mx$";

    if (email.matches(regexAlumno)) {
        System.out.println(" El correo pertenece a un ALUMNO");
    } else if (email.matches(regexDocente)) {
        System.out.println(" El correo pertenece a un DOCENTE");
    } else if (email.toLowerCase().endsWith("@uacam.mx")) {
        System.out.println("Pertenece al dominio @uacam.mx, pero la estructura del usuario no coincide.");
    } else {
        System.out.println("No es un correo institucional válido de la UACAM.");
    }
}
}