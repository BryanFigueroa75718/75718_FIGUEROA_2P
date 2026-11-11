import java.io.*;

public class EscrituraBuffered1 {

    // Si es false, las mediciones se hacen pero no se imprimen (se usa para el calentamiento de la JVM)
    private static boolean imprimir = true;

    public static void main(String[] args) {
        // Puedes pasar n como argumento: java EscrituraBuffered 100000
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        // Calentamiento de la JVM (JIT): una pasada pequeña cuyos tiempos se descartan
        imprimir = false;
        ejecutarTodo(10_000, rutaTexto, rutaBinario);

        // Medición real
        imprimir = true;
        System.out.println("n = " + n);
        System.out.println("-------------------------------------------");
        ejecutarTodo(n, rutaTexto, rutaBinario);
    }

    private static void ejecutarTodo(int n, String rutaTexto, String rutaBinario) {
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
                fw.write("x");   // un carácter por llamada
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileWriter", n, inicio);
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write("x");   // misma operación, pero pasa por el buffer
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedWriter", n, inicio);
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        long inicio = System.nanoTime();
        long leidos = 0;
        try (FileReader fr = new FileReader(ruta)) {
            while (fr.read() != -1) {   // un carácter por llamada
                leidos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileReader", leidos, inicio);
    }

    private static void leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        long leidos = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while (br.read() != -1) {   // misma operación, pero desde el buffer
                leidos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedReader", leidos, inicio);
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i % 256);   // un byte por llamada
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileOutputStream", n, inicio);
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i % 256);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedOutputStream", n, inicio);
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        long inicio = System.nanoTime();
        long leidos = 0;
        try (FileInputStream fis = new FileInputStream(ruta)) {
            while (fis.read() != -1) {
                leidos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("FileInputStream", leidos, inicio);
    }

    private static void leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        long leidos = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            while (bis.read() != -1) {
                leidos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mostrar("BufferedInputStream", leidos, inicio);
    }

    // ---------- Utilidad para mostrar el tiempo ----------

    private static void mostrar(String nombre, long elementos, long inicioNano) {
        long finNano = System.nanoTime();
        double ms = (finNano - inicioNano) / 1_000_000.0;
        if (imprimir) {
            System.out.printf("%-22s %,12d elementos  %10.2f ms%n", nombre, elementos, ms);
        }
    }
}
