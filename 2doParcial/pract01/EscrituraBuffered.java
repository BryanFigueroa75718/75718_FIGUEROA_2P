
import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.println("=== Pruebas de rendimiento de flujos (N = " + n + ") ===");

        // Flujos de texto
        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);

        // Flujos de bytes
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        // try with
        try (Writer w = new FileWriter(ruta)) {
            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                w.write("Hello World " + i + "\n");
            }
            w.flush();

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("escribirFileWriter - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        // try with
        try (Writer w = new FileWriter(ruta);
             BufferedWriter bw = new BufferedWriter(w)) {
            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                bw.write("Hello World " + i + "\n");
            }
            bw.flush();

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("escribirBufferedWriter - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        // try with
        try (Reader r = new FileReader(ruta)) {
            long inicio = System.nanoTime();

            int caracter;
            while ((caracter = r.read()) != -1) {
                // Lectura carácter por carácter sin buffer
            }

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("leerFileReader - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leerBufferedReader(String ruta) {
        // try with
        try (Reader r = new FileReader(ruta);
             BufferedReader br = new BufferedReader(r)) {
            long inicio = System.nanoTime();

            String linea;
            while ((linea = br.readLine()) != null) {
                // Lectura línea a línea con buffer
            }

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("leerBufferedReader - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        // try with
        try (OutputStream fos = new FileOutputStream(ruta)) {
            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                fos.write(i);
            }
            fos.flush();

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("escribirFileOutputStream - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        // try with
        try (OutputStream fos = new FileOutputStream(ruta);
             // Probar con la clase Object o Buffer
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                bos.write(i);
            }
            bos.flush();

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("escribirBufferedOutputStream - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        // try with
        try (InputStream fis = new FileInputStream(ruta)) {
            long inicio = System.nanoTime();

            int octeto;
            while ((octeto = fis.read()) != -1) {
                // Lectura byte a byte sin buffer
            }

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("leerFileInputStream - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leerBufferedInputStream(String ruta) {
        // try with
        try (InputStream fis = new FileInputStream(ruta);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            long inicio = System.nanoTime();

            int octeto;
            while ((octeto = bis.read()) != -1) {
                // Lectura byte a byte con buffer
            }

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("leerBufferedInputStream - Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}