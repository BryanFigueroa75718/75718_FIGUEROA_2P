package Practica06_Aaron;

import java.io.*;

// Aaron David Chi Uc

public class EscrituraBuffered {
    

    // TEXTO-ESCRITURA 
  // escritura de texto sin buffer
    public static void escribirFileWriter(String ruta, int n) {

        long inicio = System.nanoTime();

        try (Writer w = new FileWriter(ruta)) {

            for (int i = 0; i < n; i++) {
                w.write("Hola Mundo" + i + "\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("FileWriter (sin buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
    }


    // escritura de texto con buffer
    public static void escribirBufferedWriter(String ruta, int n) {

        long inicio = System.nanoTime();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {

            for (int i = 0; i < n; i++) {
                bw.write("Hola Mundo" + i);
                bw.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("BufferedWriter (con buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
    }


    // TEXTO-LECTURA 

    // lectura de texto sin buffer
    public static void leerFileReader(String ruta) {

        long inicio = System.nanoTime();
        long caracteres = 0;

        try (Reader r = new FileReader(ruta)) {

            int caracter;

            while ((caracter = r.read()) != -1) {
                caracteres++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("FileReader (sin buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
        System.out.println("Caracteres leidos: " + caracteres);
    }


    //Lectura de texto con buffer
    public static void leerBufferedReader(String ruta) {

        long inicio = System.nanoTime();
        long caracteres = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {

            int caracter;

            while ((caracter = br.read()) != -1) {
                caracteres++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("BufferedReader (con buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
        System.out.println("Caracteres leidos: " + caracteres);
    }


    // ESCRITURA 

    // scritura binaria sin buffer
    public static void escribirFileOutputStream(String ruta, int n) {

        long inicio = System.nanoTime();

        try (OutputStream fos = new FileOutputStream(ruta)) {

            for (int i = 0; i < n; i++) {
                fos.write(i);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("FileOutputStream (sin buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
    }


    //escritura binaria con buffer
    public static void escribirBufferedOutputStream(String ruta, int n) {

        long inicio = System.nanoTime();

        try (OutputStream fos = new FileOutputStream(ruta);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            for (int i = 0; i < n; i++) {
                bos.write(i);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("BufferedOutputStream (con buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
    }


    //LECTURA

    // lectura binaria sin buffer
    public static void leerFileInputStream(String ruta) {

        long inicio = System.nanoTime();
        long bytesLeidos = 0;

        try (InputStream fis = new FileInputStream(ruta)) {

            int octeto;

            while ((octeto = fis.read()) != -1) {
                bytesLeidos++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("FileInputStream (sin buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
        System.out.println("Bytes leidos: " + bytesLeidos);
    }


    // lectura binaria con buffer
    public static void leerBufferedInputStream(String ruta) {

        long inicio = System.nanoTime();
        long bytesLeidos = 0;

        try (InputStream fis = new FileInputStream(ruta);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int octeto;

            while ((octeto = bis.read()) != -1) {
                bytesLeidos++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("BufferedInputStream (con buffer): "
                + duracionNs + " ns (" + duracionMs + " ms)");
        System.out.println("Bytes leidos: " + bytesLeidos);
    }

}
