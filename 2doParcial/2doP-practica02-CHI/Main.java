import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    // Expresión regular para validar comandos de login con un correo válido
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^login\\s+([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) {
        String prompt = "> ";
        String color = "\u001B[0m"; // Color por defecto (Reset)

        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r)) {

            String ch;
            do {
                System.out.print(color + prompt);
                ch = br.readLine();

                if (ch != null && !ch.trim().equalsIgnoreCase("Exit")) {
                    String input = ch.trim();

                    // 1. Comando 'help'
                    if (input.equalsIgnoreCase("help")) {
                        System.out.println("--- Lista de comandos disponibles ---");
                        System.out.println("  help            : Muestra esta lista de comandos");
                        System.out.println("  login <correo>  : Inicia sesión con un correo válido");
                        System.out.println("  fecha           : Muestra la fecha actual");
                        System.out.println("  hora            : Muestra la hora actual");
                        System.out.println("  prompt <texto>  : Cambia el símbolo del indicador de la terminal");
                        System.out.println("  color <nombre>  : Cambia el color del texto (rojo, verde, azul, reset)");
                        System.out.println("  exit            : Sale de la aplicación");

                    // 2. Comando 'login' validado con Regex
                    } else if (LOGIN_PATTERN.matcher(input).matches()) {
                        Matcher matcher = LOGIN_PATTERN.matcher(input);
                        if (matcher.find()) {
                            String email = matcher.group(1);
                            System.out.println("Registro exitoso: " + email);
                        }

                    } else if (input.toLowerCase().startsWith("login")) {
                        System.out.println("Correo inválido. Ejemplo: login al076228@uacam.mx");

                    // 3. Dos comandos inventados (usando LocalDate y LocalTime ya importados)
                    } else if (input.equalsIgnoreCase("fecha")) {
                        System.out.println("Fecha actual: " + LocalDate.now());

                    } else if (input.equalsIgnoreCase("hora")) {
                        System.out.println("Hora actual: " + LocalTime.now());

                    // 4. Personalizar terminal (símbolo de prompt y colores ANSI)
                    } else if (input.toLowerCase().startsWith("prompt ")) {
                        prompt = input.substring(7) + " ";
                        System.out.println("Prompt actualizado.");

                    } else if (input.toLowerCase().startsWith("color ")) {
                        String col = input.substring(6).toLowerCase();
                        switch (col) {
                            case "rojo": color = "\u001B[31m"; break;
                            case "verde": color = "\u001B[32m"; break;
                            case "azul": color = "\u001B[34m"; break;
                            case "reset": color = "\u001B[0m"; break;
                            default: System.out.println("Color no válido. Opciones: rojo, verde, azul, reset"); break;
                        }

                    } else {
                        System.out.println("Comando no reconocido. Escribe 'help' para ver las opciones.");
                    }
                }
            } while (ch != null && !ch.trim().equalsIgnoreCase("Exit"));

            System.out.println("\u001B[0mSaliendo de la terminal...");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}