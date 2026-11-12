import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = 1_000_000; 
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.println("--- INICIANDO PRUEBAS CON n = " + n + " ---");

        long inicio = System.nanoTime();
        escribirFileWriter(rutaTexto, n);
        long fin = System.nanoTime();
        imprimirTiempo("Escritura FileWriter", inicio, fin);

        inicio = System.nanoTime();
        escribirBufferedWriter(rutaTexto, n);
        fin = System.nanoTime();
        imprimirTiempo("Escritura BufferedWriter", inicio, fin);

        inicio = System.nanoTime();
        leerFileReader(rutaTexto);
        fin = System.nanoTime();
        imprimirTiempo("Lectura FileReader", inicio, fin);

        inicio = System.nanoTime();
        leerBufferedReader(rutaTexto);
        fin = System.nanoTime();
        imprimirTiempo("Lectura BufferedReader", inicio, fin);

        inicio = System.nanoTime();
        escribirFileOutputStream(rutaBinario, n);
        fin = System.nanoTime();
        imprimirTiempo("Escritura FileOutputStream", inicio, fin);

        inicio = System.nanoTime();
        escribirBufferedOutputStream(rutaBinario, n);
        fin = System.nanoTime();
        imprimirTiempo("Escritura BufferedOutputStream", inicio, fin);

        // --- BINARIO: LECTURA ---
        inicio = System.nanoTime();
        leerFileInputStream(rutaBinario);
        fin = System.nanoTime();
        imprimirTiempo("Lectura FileInputStream", inicio, fin);

        inicio = System.nanoTime();
        leerBufferedInputStream(rutaBinario);
        fin = System.nanoTime();
        imprimirTiempo("Lectura BufferedInputStream", inicio, fin);
    }

    private static void imprimirTiempo(String operacion, long inicio, long fin) {
        long duracionMs = (fin - inicio) / 1_000_000;
        System.out.printf("%-30s : %d ms%n", operacion, duracionMs);
    }


    private static void escribirFileWriter(String ruta, int n) {
        try (FileWriter fw = new FileWriter(ruta)) {
            for (int i = 0; i < n; i++) {
                fw.write("Linea de prueba " + i + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error en FileWriter: " + e.getMessage());
        }
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                bw.write("Linea de prueba " + i + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error en BufferedWriter: " + e.getMessage());
        }
    }


    private static void leerFileReader(String ruta) {
        try (FileReader fr = new FileReader(ruta)) {
            int caracter;
            while ((caracter = fr.read()) != -1) {
            }
        } catch (IOException e) {
            System.err.println("Error en FileReader: " + e.getMessage());
        }
    }

    private static void leerBufferedReader(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
            }
        } catch (IOException e) {
            System.err.println("Error en BufferedReader: " + e.getMessage());
        }
    }


    private static void escribirFileOutputStream(String ruta, int n) {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                fos.write(i); 
            }
        } catch (IOException e) {
            System.err.println("Error en FileOutputStream: " + e.getMessage());
        }
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                bos.write(i);
            }
        } catch (IOException e) {
            System.err.println("Error en BufferedOutputStream: " + e.getMessage());
        }
    }


    private static void leerFileInputStream(String ruta) {
        try (FileInputStream fis = new FileInputStream(ruta)) {
            int byteLeido;
            while ((byteLeido = fis.read()) != -1) {
            }
        } catch (IOException e) {
            System.err.println("Error en FileInputStream: " + e.getMessage());
        }
    }

    private static void leerBufferedInputStream(String ruta) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta))) {
            int byteLeido;
            while ((byteLeido = bis.read()) != -1) {
            }
        } catch (IOException e) {
            System.err.println("Error en BufferedInputStream: " + e.getMessage());
        }
    }
}
