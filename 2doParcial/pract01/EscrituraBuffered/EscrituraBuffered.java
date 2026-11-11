import java.io.*;

public class  EscrituraBuffered  {

    public static void main(String[] args) {
        int n = 10_000_000;
        System.out.println("n = " + n);
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

    public static void escribirFileWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) {
                fw.write("Hello world " + i + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("FileWriter: " + (fin - inicio) / 1_000_000.0 + " ms");
    }

    public static void escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write("Hello world " + i + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("BufferedWriter: " + (fin - inicio) / 1_000_000.0 + " ms");
    }

    public static void leerFileReader(String ruta) {
        long inicio = System.nanoTime();
        try (FileReader fr = new FileReader(ruta)) {
            while (fr.read() != -1) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("FileReader: " + (fin - inicio) / 1_000_000.0 + " ms");
    }

    public static void leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while (br.readLine() != null) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("BufferedReader: " + (fin - inicio) / 1_000_000.0 + " ms");
    }

    public static void escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("FileOutputStream: " + (fin - inicio) / 1_000_000.0 + " ms");
    }

    public static void escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("BufferedOutputStream: " + (fin - inicio) / 1_000_000.0 + " ms");
    }

    public static void leerFileInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (FileInputStream fis = new FileInputStream(ruta)) {
            while (fis.read() != -1) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("FileInputStream: " + (fin - inicio) / 1_000_000.0 + " ms");
    }

    public static void leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            while (bis.read() != -1) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        System.out.println("BufferedInputStream: " + (fin - inicio) / 1_000_000.0 + " ms");
    }
}