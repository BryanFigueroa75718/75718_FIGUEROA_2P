import java.io.*;

/**
 * Práctica 01: medición del tiempo de ejecución con flujos con y sin buffer.
 *
 * Uso:
 *   javac EscrituraBuffered.java
 *   java EscrituraBuffered            (n = 1_000_000 por defecto)
 *   java EscrituraBuffered 100000     (n indicado por argumento)
 *
 * Cada método mide con System.nanoTime() TODO el bloque try-with-resources,
 * es decir, incluye el vaciado (flush) y el cierre final del flujo.
 */
public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";
        // Calentamiento de la JVM (JIT): se ejecuta todo con pocos datos y se descarta.
        ejecutarTodo(10_000, rutaTexto, rutaBinario, false);

        // Medición real
        System.out.println("Flujo;n;tiempo_ms");
        ejecutarTodo(n, rutaTexto, rutaBinario, true);

        new File(rutaTexto).delete();
        new File(rutaBinario).delete();
    }

    private static void ejecutarTodo(int n, String rutaTexto, String rutaBinario, boolean imprimir) {
        long[] t = new long[8];
        t[0] = escribirFileWriter(rutaTexto, n);
        t[1] = escribirBufferedWriter(rutaTexto, n);
        t[2] = leerFileReader(rutaTexto);
        t[3] = leerBufferedReader(rutaTexto);
        t[4] = escribirFileOutputStream(rutaBinario, n);
        t[5] = escribirBufferedOutputStream(rutaBinario, n);
        t[6] = leerFileInputStream(rutaBinario);
        t[7] = leerBufferedInputStream(rutaBinario);

        if (imprimir) {
            String[] nombres = {"FileWriter", "BufferedWriter", "FileReader", "BufferedReader",
                    "FileOutputStream", "BufferedOutputStream", "FileInputStream", "BufferedInputStream"};
            for (int i = 0; i < 8; i++) {
                System.out.println(nombres[i] + ";" + n + ";" + (t[i] / 1_000_000.0));
            }
        }
    }

    // ---------- Texto: escritura ----------

    private static long escribirFileWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) {
                fw.write(i + "\n");          // un elemento = un número y salto de línea
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return System.nanoTime() - inicio;
    }

    private static long escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write(i + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return System.nanoTime() - inicio;
    }

    // ---------- Texto: lectura (carácter por carácter, igual en ambos) ----------

    private static long leerFileReader(String ruta) {
        long suma = 0;
        long inicio = System.nanoTime();
        try (FileReader fr = new FileReader(ruta)) {
            int c;
            while ((c = fr.read()) != -1) {
                suma += c;                   // se usa el dato para que el JIT no elimine la lectura
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        if (suma == -1) System.out.println(suma);
        return fin - inicio;
    }

    private static long leerBufferedReader(String ruta) {
        long suma = 0;
        long inicio = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            int c;
            while ((c = br.read()) != -1) {
                suma += c;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        if (suma == -1) System.out.println(suma);
        return fin - inicio;
    }

    // ---------- Binario: escritura (un byte por elemento) ----------

    private static long escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i);                // cada write(int) es una llamada al sistema
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return System.nanoTime() - inicio;
    }

    private static long escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i);                // se acumula en el buffer de 8 KB
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return System.nanoTime() - inicio;
    }

    // ---------- Binario: lectura (un byte por llamada) ----------

    private static long leerFileInputStream(String ruta) {
        long suma = 0;
        long inicio = System.nanoTime();
        try (FileInputStream fis = new FileInputStream(ruta)) {
            int b;
            while ((b = fis.read()) != -1) {
                suma += b;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        if (suma == -1) System.out.println(suma);
        return fin - inicio;
    }

    private static long leerBufferedInputStream(String ruta) {
        long suma = 0;
        long inicio = System.nanoTime();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            int b;
            while ((b = bis.read()) != -1) {
                suma += b;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long fin = System.nanoTime();
        if (suma == -1) System.out.println(suma);
        return fin - inicio;
    }
}
