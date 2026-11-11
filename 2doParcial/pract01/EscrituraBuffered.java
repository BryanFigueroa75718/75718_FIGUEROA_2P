import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        // n por defecto, también se puede pasar como argumento
        int n = 1_000_000;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.println("n = " + n);

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // calcula y muestra el tiempo en ms
    private static void imprimir(String nombre, long inicio) {
        long fin = System.nanoTime();
        double ms = (fin - inicio) / 1_000_000.0;
        System.out.println(nombre + ": " + ms + " ms");
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
        imprimir("FileWriter", inicio);
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write(i + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imprimir("BufferedWriter", inicio);
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        long inicio = System.nanoTime();
        try (FileReader fr = new FileReader(ruta)) {
            int c;
            while ((c = fr.read()) != -1) {
                // solo se lee
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imprimir("FileReader", inicio);
    }

    private static void leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            int c;
            while ((c = br.read()) != -1) {
                // solo se lee
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imprimir("BufferedReader", inicio);
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imprimir("FileOutputStream", inicio);
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imprimir("BufferedOutputStream", inicio);
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (FileInputStream fis = new FileInputStream(ruta)) {
            int b;
            while ((b = fis.read()) != -1) {
                // solo se lee
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imprimir("FileInputStream", inicio);
    }

    private static void leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            int b;
            while ((b = bis.read()) != -1) {
                // solo se lee
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        imprimir("BufferedInputStream", inicio);
    }
}
