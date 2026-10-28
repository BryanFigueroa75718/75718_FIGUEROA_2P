import java.io.*;

public class Act_Stream {

    public static void escribir() {
        try (OutputStream fos = new FileOutputStream("data.txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeUTF("Hello World");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void escribir_mucho() {
        try (OutputStream fos = new FileOutputStream("data.txt");
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            long inicio = System.nanoTime();   // empieza a medir

            for (int i = 0; i < 1_000; i++) {  // cámbialo a 10_000_000 cuando quieras medir en serio
                bos.write(i);
            }

            long fin = System.nanoTime();      // termina de medir
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;

            System.out.println("Duración: " + duracionNs + " ns (" + duracionMs + " ms)");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void leer() {
        try (FileInputStream fis = new FileInputStream("data.txt");
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int octeto;
            int contador = 0;

            while ((octeto = bis.read()) != -1) {
                if (contador % 30 == 0) {
                    System.out.println();
                }
                System.out.print(octeto + " ");
                contador++;
            }
            System.out.println();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

//    public static void guardarNota (String nota) {
//        System.out.println("guardar Nota");
//    }
//
//    public static String leerNotas() {
//        return "notas";
//    }

