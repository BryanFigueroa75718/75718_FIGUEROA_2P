import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.println("n = " + n);
        System.out.println("---------------------------------------------");

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // ---------- Utilidad para imprimir el tiempo ----------

    private static void mostrar(String flujo, long inicio) {
        double ms = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.printf("%-22s %10.3f ms%n", flujo, ms);
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) {
                fw.write(i + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileWriter", inicio);
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write(String.valueOf(i));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedWriter", inicio);
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        long inicio = System.nanoTime();
        long caracteres = 0;
        try (FileReader fr = new FileReader(ruta)) {
            while (fr.read() != -1) {
                caracteres++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileReader", inicio);
    }

    private static void leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        long caracteres = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while (br.read() != -1) {
                caracteres++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedReader", inicio);
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i & 0xFF);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileOutputStream", inicio);
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i & 0xFF);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedOutputStream", inicio);
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        long inicio = System.nanoTime();
        long bytes = 0;
        try (FileInputStream fis = new FileInputStream(ruta)) {
            while (fis.read() != -1) {
                bytes++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileInputStream", inicio);
    }

    private static void leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        long bytes = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            while (bis.read() != -1) {
                bytes++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedInputStream", inicio);
    }
}
