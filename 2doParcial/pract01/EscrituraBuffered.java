/*
 * Práctica 01 - Medición de tiempo de ejecución con flujos con y sin buffer
 * Materia: Concurrencia y Paralelismo
 * Alumno: César Enrique Uc Aguilar
 * Ingeniería en Tecnología en Software - Universidad Autónoma de Campeche
 */
package pract01;

import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        // Se puede pasar n como argumento: java EscrituraBuffered 100000
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.println("n = " + n);
        System.out.println("----------------------------------------");

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

    private static void mostrar(String flujo, long inicio, long fin) {
        double ms = (fin - inicio) / 1_000_000.0;
        System.out.printf("%-22s %12.3f ms%n", flujo, ms);
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) {
                fw.write("Linea " + i + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mostrar("FileWriter", inicio, System.nanoTime());
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write("Linea " + i + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mostrar("BufferedWriter", inicio, System.nanoTime());
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
            throw new RuntimeException(e);
        }
        mostrar("FileReader", inicio, System.nanoTime());
    }

    private static void leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        long caracteres = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while (br.read() != -1) {
                caracteres++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mostrar("BufferedReader", inicio, System.nanoTime());
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mostrar("FileOutputStream", inicio, System.nanoTime());
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mostrar("BufferedOutputStream", inicio, System.nanoTime());
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
            throw new RuntimeException(e);
        }
        mostrar("FileInputStream", inicio, System.nanoTime());
    }

    private static void leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        long bytes = 0;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            while (bis.read() != -1) {
                bytes++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mostrar("BufferedInputStream", inicio, System.nanoTime());
    }
}
