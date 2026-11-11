import java.io.*;

public class Act_Stream {

    public static void escribir() {
        try (OutputStream fos = new FileOutputStream("data.txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeUTF("Hello World");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

    /*public static void escribir_mucho() {
        long inicio = System.nanoTime();

        try (OutputStream fos = new FileOutputStream("data.txt");
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            for (int i = 0; i < 10_000_000; i++) {
                bos.write(i);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long fin = System.nanoTime();
        long duracionNs = fin - inicio;
        double duracionMs = duracionNs / 1_000_000.0;

        System.out.println("Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
    }

    public static void leer() {
        try (FileInputStream fis = new FileInputStream("data.txt")) {
            int octeto = 0;
            while (octeto != -1) {
                octeto = fis.read();
                if (octeto != -1) {
                    System.out.print(octeto + " ");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

     */