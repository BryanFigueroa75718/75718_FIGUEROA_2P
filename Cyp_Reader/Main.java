import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {

        try (Reader r = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(r);
        ) {
            // Pedir el nombre
            System.out.print("¿Cómo te llamas? ");
            String nombre = br.readLine();

            // Fecha y hora actual
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter fecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm:ss");

            System.out.println("Nombre: " + nombre);
            System.out.println("Fecha: " + ahora.format(fecha));
            System.out.println("Hora: " + ahora.format(hora));
            System.out.println();

            String ch;
            System.out.print("> ");
            do {
                ch = br.readLine();
                if (ch != null && !ch.equalsIgnoreCase("exit()")) {
                    System.out.println("> comando" + ch.length());
                }
            } while (ch != null && !ch.equalsIgnoreCase("exit()"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}