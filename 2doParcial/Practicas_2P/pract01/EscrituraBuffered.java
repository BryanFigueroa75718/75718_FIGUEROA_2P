/* Gladys Candy Chumá Pérez - 70238
* CONCURRENCIA Y PARALELISMO
* Práctica 1 II Parcial*/


package pract01;

import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {

        // Cantidad de datos que se van a utilizar en las pruebas
        int n = 10_000_000;

        // Nombre de los archivos donde se guardaran los datos
        String rutaTexto = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        System.out.println("--Texto--");

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);

        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);

        System.out.println("\n--Binarios--");

        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);

        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // Escribe los datos en un archivo de texto sin utilizar buffer
    private static void escribirFileWriter(String ruta, int cantidad) {

        long inicio = System.nanoTime();

        try (FileWriter escritor = new FileWriter(ruta)) {

            for (int i = 0; i < cantidad; i++) {
                escritor.write("Registro numero " + i + "\n");
            }

        } catch (IOException e) {
            System.out.println("Error al escribir: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("FileWriter: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // Hace lo mismo que el anterior pero ahora utiliza un buffer
    private static void escribirBufferedWriter(String ruta, int cantidad) {

        long inicio = System.nanoTime();

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter(ruta))) {

            for (int i = 0; i < cantidad; i++) {
                escritor.write("Registro número " + i);
                escritor.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error al escribir: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedWriter: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // Lee el archivo de texto caracter por caracter sin buffer
    private static void leerFileReader(String ruta) {

        long inicio = System.nanoTime();

        try (FileReader lector = new FileReader(ruta)) {

            while (lector.read() != -1) {
                // Solo estamos leyendo para medir el tiempo
            }

        } catch (IOException e) {
            System.out.println("Error al leer: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("FileReader: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // Lee el mismo archivo utilizando un buffer
    private static void leerBufferedReader(String ruta) {

        long inicio = System.nanoTime();

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(ruta))) {

            while (lector.readLine() != null) {
                // Se lee una linea completa en cada vuelta
            }

        } catch (IOException e) {
            System.out.println("Error al leer: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedReader: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // Escribe los datos como bytes directamente en el archivo
    private static void escribirFileOutputStream(String ruta, int cantidad) {

        long inicio = System.nanoTime();

        try (FileOutputStream escritor = new FileOutputStream(ruta)) {

            for (int i = 0; i < cantidad; i++) {
                escritor.write(i % 256);
            }

        } catch (IOException e) {
            System.out.println("Error al escribir bytes: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("FileOutputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // Escribe los mismos bytes pero utilizando un buffer
    private static void escribirBufferedOutputStream(String ruta, int cantidad) {

        long inicio = System.nanoTime();

        try (BufferedOutputStream escritor =
                     new BufferedOutputStream(new FileOutputStream(ruta))) {

            for (int i = 0; i < cantidad; i++) {
                escritor.write(i % 256);
            }

        } catch (IOException e) {
            System.out.println("Error al escribir bytes: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedOutputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // Lee los bytes del archivo sin utilizar buffer
    private static void leerFileInputStream(String ruta) {

        long inicio = System.nanoTime();

        try (FileInputStream lector = new FileInputStream(ruta)) {

            while (lector.read() != -1) {
                // Se leen los bytes para medir el tiempo
            }

        } catch (IOException e) {
            System.out.println("Error al leer bytes: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("FileInputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }

    // Lee los bytes utilizando un buffer para hacerlo por bloques
    private static void leerBufferedInputStream(String ruta) {

        long inicio = System.nanoTime();

        try (BufferedInputStream lector =
                     new BufferedInputStream(new FileInputStream(ruta))) {

            while (lector.read() != -1) {
                // Se leen los bytes para medir el tiempo
            }

        } catch (IOException e) {
            System.out.println("Error al leer bytes: " + e.getMessage());
            return;
        }

        long fin = System.nanoTime();

        System.out.printf("BufferedInputStream: %.3f ms%n",
                (fin - inicio) / 1_000_000.0);
    }
}
