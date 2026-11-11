import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = 10_000_000; 
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        String modo = args.length > 0 ? args[0] : "todos";

        System.out.println("=== Modo: " + modo + " | n = " + n + " ===");

        switch (modo) {
            case "sinBuffer":
                escribirFileWriter(rutaTexto, n);
                leerFileReader(rutaTexto);
                escribirFileOutputStream(rutaBinario, n);
                leerFileInputStream(rutaBinario);
                break;
            case "conBuffer":
                escribirBufferedWriter(rutaTexto, n);
                leerBufferedReader(rutaTexto);
                escribirBufferedOutputStream(rutaBinario, n);
                leerBufferedInputStream(rutaBinario);
                break;
            default:
                escribirFileWriter(rutaTexto, n);
                escribirBufferedWriter(rutaTexto, n);
                leerFileReader(rutaTexto);
                leerBufferedReader(rutaTexto);
                escribirFileOutputStream(rutaBinario, n);
                escribirBufferedOutputStream(rutaBinario, n);
                leerFileInputStream(rutaBinario);
                leerBufferedInputStream(rutaBinario);
        }
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) fw.write("Linea " + i + "\n");
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("FileWriter          escritura: %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) bw.write("Linea " + i + "\n");
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("BufferedWriter      escritura: %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        long inicio = System.nanoTime();
        try (FileReader fr = new FileReader(ruta)) {
            while (fr.read() != -1) { }
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("FileReader          lectura  : %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }

    private static void leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while (br.readLine() != null) { }
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("BufferedReader      lectura  : %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) fos.write((i + "\n").getBytes());
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("FileOutputStream    escritura: %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) bos.write((i + "\n").getBytes());
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("BufferedOutputStream escritura: %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (FileInputStream fis = new FileInputStream(ruta)) {
            while (fis.read() != -1) { }
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("FileInputStream     lectura  : %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }

    private static void leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            while (bis.read() != -1) { }
        } catch (IOException e) { e.printStackTrace(); }
        long fin = System.nanoTime();
        System.out.printf("BufferedInputStream lectura  : %8.3f ms%n", (fin - inicio) / 1_000_000.0);
    }
}