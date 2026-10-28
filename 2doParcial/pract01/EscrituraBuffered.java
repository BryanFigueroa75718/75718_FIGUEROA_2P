/*Daniel Beytia, 

Práctica 01 Concurrencia y paralelismo*/
//Actulizacion de Repo

package pract01;

import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = 1_000_000;
        // Puedes pasar n como argumento: java pract01.EscrituraBuffered 100000
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
        }
        String rutaTexto   = "pract01/archivo_salida.txt";
        String rutaBinario = "pract01/archivo_salida.dat";

        System.out.println("n = " + n);
        System.out.println("---- TEXTO ----");
        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);

        System.out.println("---- BINARIO ----");
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // Muestra el tiempo en ns y ms
    private static void imprimir(String nombre, long inicio, long fin) {
        long ns = fin - inicio;
        System.out.printf("%-24s %,15d ns  (%.3f ms)%n", nombre, ns, ns / 1_000_000.0);
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        try (FileWriter fw = new FileWriter(ruta)) {
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                fw.write('A');              // un caracter por llamada
            }
            long fin = System.nanoTime();
            imprimir("FileWriter", inicio, fin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                bw.write('A');
            }
            bw.flush();                     // vacia el buffer antes de medir
            long fin = System.nanoTime();
            imprimir("BufferedWriter", inicio, fin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        try (FileReader fr = new FileReader(ruta)) {
            long inicio = System.nanoTime();
            long cuenta = 0;
            while (fr.read() != -1) {       // un caracter por llamada
                cuenta++;
            }
            long fin = System.nanoTime();
            imprimir("FileReader", inicio, fin);
            System.out.println("   caracteres leidos: " + cuenta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leerBufferedReader(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            long inicio = System.nanoTime();
            long cuenta = 0;
            while (br.read() != -1) {
                cuenta++;
            }
            long fin = System.nanoTime();
            imprimir("BufferedReader", inicio, fin);
            System.out.println("   caracteres leidos: " + cuenta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                fos.write(i & 0xFF);        // un byte por llamada
            }
            long fin = System.nanoTime();
            imprimir("FileOutputStream", inicio, fin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            long inicio = System.nanoTime();
            for (int i = 0; i < n; i++) {
                bos.write(i & 0xFF);
            }
            bos.flush();
            long fin = System.nanoTime();
            imprimir("BufferedOutputStream", inicio, fin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        try (FileInputStream fis = new FileInputStream(ruta)) {
            long inicio = System.nanoTime();
            long cuenta = 0;
            while (fis.read() != -1) {      // un byte por llamada
                cuenta++;
            }
            long fin = System.nanoTime();
            imprimir("FileInputStream", inicio, fin);
            System.out.println("   bytes leidos: " + cuenta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leerBufferedInputStream(String ruta) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            long inicio = System.nanoTime();
            long cuenta = 0;
            while (bis.read() != -1) {
                cuenta++;
            }
            long fin = System.nanoTime();
            imprimir("BufferedInputStream", inicio, fin);
            System.out.println("   bytes leidos: " + cuenta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
