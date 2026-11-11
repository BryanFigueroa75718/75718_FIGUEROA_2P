import java.io.*;

/**
 * Práctica 01 - Medición de tiempo de ejecución con flujos con y sin buffer.
 *
 * Cada método escribe o lee el archivo elemento por elemento y devuelve el
 * tiempo transcurrido en milisegundos medido con System.nanoTime().
 * La medición incluye la apertura y el cierre del flujo (try-with-resources),
 * porque el cierre provoca el volcado (flush) final del buffer al disco.
 *
 * 
 *    
 */
public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = (args.length > 0) ? Integer.parseInt(args[0].replace("_", "")) : 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.printf("n = %,d elementos%n", n);
        System.out.println("------------------------------------------------");

        // ---------- Texto ----------
        imprimir("FileWriter",           n, escribirFileWriter(rutaTexto, n));
        imprimir("BufferedWriter",       n, escribirBufferedWriter(rutaTexto, n));
        imprimir("FileReader",           n, leerFileReader(rutaTexto));
        imprimir("BufferedReader",       n, leerBufferedReader(rutaTexto));

        // ---------- Binario ----------
        imprimir("FileOutputStream",     n, escribirFileOutputStream(rutaBinario, n));
        imprimir("BufferedOutputStream", n, escribirBufferedOutputStream(rutaBinario, n));
        imprimir("FileInputStream",      n, leerFileInputStream(rutaBinario));
        imprimir("BufferedInputStream",  n, leerBufferedInputStream(rutaBinario));

        System.out.println("------------------------------------------------");
        System.out.printf("Tamaño %s: %,d bytes%n", rutaTexto,   new File(rutaTexto).length());
        System.out.printf("Tamaño %s: %,d bytes%n", rutaBinario, new File(rutaBinario).length());
    }

    private static void imprimir(String flujo, int n, double ms) {
        // Formato "RESULT;flujo;n;ms" para poder procesar la salida fácilmente
        System.out.printf("%-22s %12.3f ms%n", flujo, ms);
        System.out.printf(java.util.Locale.ROOT, "RESULT;%s;%d;%.3f%n", flujo, n, ms);
    }

    private static double aMs(long inicio, long fin) {
        return (fin - inicio) / 1_000_000.0;
    }

    // Variable "sumidero": evita que el JIT elimine las lecturas por no usarse.
    private static long sumidero = 0;

   

    private static double escribirFileWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) {
                fw.write("Hello World" + i + "\n");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return aMs(inicio, System.nanoTime());
    }

    private static double escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write("Hello World" + i);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return aMs(inicio, System.nanoTime());
    }

   
    /** Lee carácter por carácter con FileReader.read(). */
    private static double leerFileReader(String ruta) {
        long inicio = System.nanoTime();
        long lineas = 0;
        try (FileReader fr = new FileReader(ruta)) {
            int c;
            while ((c = fr.read()) != -1) {
                if (c == '\n') lineas++;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        double ms = aMs(inicio, System.nanoTime());
        sumidero += lineas;
        return ms;
    }

    /** Lee línea por línea con BufferedReader.readLine(). */
    private static double leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        long lineas = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas++;
                sumidero += linea.length();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        double ms = aMs(inicio, System.nanoTime());
        sumidero += lineas;
        return ms;
    }

  
    private static double escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i);           // escribe el byte menos significativo de i
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return aMs(inicio, System.nanoTime());
    }

    private static double escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return aMs(inicio, System.nanoTime());
    }

    private static double leerFileInputStream(String ruta) {
        long inicio = System.nanoTime();
        long suma = 0;
        try (FileInputStream fis = new FileInputStream(ruta)) {
            int b;
            while ((b = fis.read()) != -1) {
                suma += b;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        double ms = aMs(inicio, System.nanoTime());
        sumidero += suma;
        return ms;
    }

    private static double leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        long suma = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            int b;
            while ((b = bis.read()) != -1) {
                suma += b;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        double ms = aMs(inicio, System.nanoTime());
        sumidero += suma;
        return ms;
    }
}
