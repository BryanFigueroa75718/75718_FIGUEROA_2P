//Erbet Gomez Bohorquez 69338

import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = 100_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.println("Iniciando pruebas con n = " + n + " elementos\n");

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);
        
        System.out.println("-------------------------------------------------");
        
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // escritura de texto

    private static void escribirFileWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) {
                fw.write('A');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("FileWriter: \t\t" + ((fin - inicio) / 1_000_000) + " ms");
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write('A');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("BufferedWriter: \t" + ((fin - inicio) / 1_000_000) + " ms");
    }

    //lectura de texto
    private static void leerFileReader(String ruta) {
        long inicio = System.nanoTime();
        try (FileReader fr = new FileReader(ruta)) {
            int c;
            while ((c = fr.read()) != -1) {
                // Lectura caracter por caracter
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("FileReader: \t\t" + ((fin - inicio) / 1_000_000) + " ms");
    }

    private static void leerBufferedReader(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            int c;
            while ((c = br.read()) != -1) {
                // Lectura caracter por caracter desde el buffer
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("BufferedReader: \t" + ((fin - inicio) / 1_000_000) + " ms");
    }

    // escritura de binario

    private static void escribirFileOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("FileOutputStream: \t" + ((fin - inicio) / 1_000_000) + " ms");
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long inicio = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("BufferedOutputStream: \t" + ((fin - inicio) / 1_000_000) + " ms");
    }

    // lectura de binario

    private static void leerFileInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (FileInputStream fis = new FileInputStream(ruta)) {
            int b;
            while ((b = fis.read()) != -1) {
                // Lectura byte por byte
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("FileInputStream: \t" + ((fin - inicio) / 1_000_000) + " ms");
    }

    private static void leerBufferedInputStream(String ruta) {
        long inicio = System.nanoTime();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            int b;
            while ((b = bis.read()) != -1) {
                // Lectura byte por byte desde el buffer
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long fin = System.nanoTime();
        System.out.println("BufferedInputStream: \t" + ((fin - inicio) / 1_000_000) + " ms");
    }
}