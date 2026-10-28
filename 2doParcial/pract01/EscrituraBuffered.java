import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
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

    private static void reporte(String nombre, long inicio) {
        double ms = (System.nanoTime() - inicio) / 100_000.0;
        System.out.printf("%-22s %.3f ms%n", nombre, ms);
    }

    // ---------- Texto: escritura ----------
    private static void escribirFileWriter(String ruta, int n) {
        long ini = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) fw.write("Hello World" + i + "\n");
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("FileWriter", ini);      // Este esta afuera del try: incluye close()
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long ini = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) bw.write("Hello World" + i + "\n");
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("BufferedWriter", ini);
    }

    // ---------- Texto: lectura ----------
    private static void leerFileReader(String ruta) {
        long ini = System.nanoTime();
        long total = 0;
        try (FileReader fr = new FileReader(ruta)) {
            while (fr.read() != -1) total++;
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("FileReader (" + total + " chars)", ini);
    }

    private static void leerBufferedReader(String ruta) {
        long ini = System.nanoTime();
        long total = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while (br.read() != -1) total++;   // ACa tenemos el mismo método, comparación justa
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("BufferedReader (" + total + " chars)", ini);
    }

    // ---------- Binario: escritura ----------
    private static void escribirFileOutputStream(String ruta, int n) {
        long ini = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) fos.write(i);
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("FileOutputStream", ini);
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long ini = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) bos.write(i);
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("BufferedOutputStream", ini);
    }

    // ---------- Binario: lectura ----------
    private static void leerFileInputStream(String ruta) {
        long ini = System.nanoTime();
        long total = 0;
        try (FileInputStream fis = new FileInputStream(ruta)) {
            while (fis.read() != -1) total++;
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("FileInputStream (" + total + " bytes)", ini);
    }

    private static void leerBufferedInputStream(String ruta) {
        long ini = System.nanoTime();
        long total = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            while (bis.read() != -1) total++;
        } catch (IOException e) { throw new UncheckedIOException(e); }
        reporte("BufferedInputStream (" + total + " bytes)", ini);
    }
}