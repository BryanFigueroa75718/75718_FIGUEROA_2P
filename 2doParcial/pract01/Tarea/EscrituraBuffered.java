import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {

        int n = 10_000_000;

        String rutaTexto = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);

        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);

        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);

        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {

        long inicio = System.nanoTime();

        try (FileWriter fw = new FileWriter(ruta)) {

            for (int i = 0; i < n; i++) {
                fw.write("Dato " + i + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("FileWriter: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    private static void escribirBufferedWriter(String ruta, int n) {

        long inicio = System.nanoTime();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {

            for (int i = 0; i < n; i++) {
                bw.write("Dato " + i);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedWriter: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {

        long inicio = System.nanoTime();

        try (FileReader fr = new FileReader(ruta)) {

            int caracter;

            while ((caracter = fr.read()) != -1) {
                // Se lee el archivo carácter por carácter
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("FileReader: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    private static void leerBufferedReader(String ruta) {

        long inicio = System.nanoTime();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            while (br.readLine() != null) {
                // Se lee una línea completa
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedReader: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {

        long inicio = System.nanoTime();

        try (FileOutputStream fos = new FileOutputStream(ruta)) {

            for (int i = 0; i < n; i++) {
                fos.write(i & 0xFF);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("FileOutputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {

        long inicio = System.nanoTime();

        try (BufferedOutputStream bos =
                     new BufferedOutputStream(new FileOutputStream(ruta))) {

            for (int i = 0; i < n; i++) {
                bos.write(i & 0xFF);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedOutputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {

        long inicio = System.nanoTime();

        try (FileInputStream fis = new FileInputStream(ruta)) {

            while (fis.read() != -1) {
                // Se lee byte por byte
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("FileInputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    private static void leerBufferedInputStream(String ruta) {

        long inicio = System.nanoTime();

        try (BufferedInputStream bis =
                     new BufferedInputStream(new FileInputStream(ruta))) {

            while (bis.read() != -1) {
                // Se lee usando el buffer
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedInputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }
}