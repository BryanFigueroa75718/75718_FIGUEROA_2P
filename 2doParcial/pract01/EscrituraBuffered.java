import java.io.*;
import java.nio.charset.StandardCharsets;

public class EscrituraBuffered {
    public static void main(String[] args) throws IOException {
        int n = 100_000; 
        int repeticiones = 3;
        String rutaTexto = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        for (int i = 1; i <= repeticiones; i++) {
            System.out.println("\nRepeticion " + i + " - Elementos: " + n);
            escribirFileWriter(rutaTexto, n);
            escribirBufferedWriter(rutaTexto, n);
            leerFileReader(rutaTexto);
            leerBufferedReader(rutaTexto);
            escribirFileOutputStream(rutaBinario, n);
            escribirBufferedOutputStream(rutaBinario, n);
            leerFileInputStream(rutaBinario);
            leerBufferedInputStream(rutaBinario);
        }
    }
    private static void escribirFileWriter(String ruta, int n) throws IOException {
        long inicio = System.nanoTime();
        try (FileWriter flujo = new FileWriter(ruta, StandardCharsets.UTF_8)) {
            for (int i = 0; i < n; i++) {
                flujo.write(65 + i % 26); 
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("FileWriter: " + tiempo + " ms");
    }
    private static void escribirBufferedWriter(String ruta, int n) throws IOException {
        long inicio = System.nanoTime();
        try (BufferedWriter flujo = new BufferedWriter(new FileWriter(ruta, StandardCharsets.UTF_8))) {
            for (int i = 0; i < n; i++) {
                flujo.write(65 + i % 26); 
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("BufferedWriter: " + tiempo + " ms");
    }
    private static void leerFileReader(String ruta) throws IOException {
        long inicio = System.nanoTime();
        try (FileReader flujo = new FileReader(ruta, StandardCharsets.UTF_8)) {
            while (flujo.read() != -1) {
        
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("FileReader: " + tiempo + " ms");
    }
    private static void leerBufferedReader(String ruta) throws IOException {
        long inicio = System.nanoTime();
        try (BufferedReader flujo = new BufferedReader(new FileReader(ruta, StandardCharsets.UTF_8))) {
            while (flujo.read() != -1) {
               
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("BufferedReader: " + tiempo + " ms");
    }
    private static void escribirFileOutputStream(String ruta, int n) throws IOException {
        long inicio = System.nanoTime();
        try (FileOutputStream flujo = new FileOutputStream(ruta)) {
            for (int i = 0; i < n; i++) {
                flujo.write(65 + i % 26); 
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("FileOutputStream: " + tiempo + " ms");
    }
    private static void escribirBufferedOutputStream(String ruta, int n) throws IOException {
        long inicio = System.nanoTime();
        try (BufferedOutputStream flujo = new BufferedOutputStream(new FileOutputStream(ruta))) {
            for (int i = 0; i < n; i++) {
                flujo.write(65 + i % 26); 
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("BufferedOutputStream: " + tiempo + " ms");
    }
    private static void leerFileInputStream(String ruta) throws IOException {
        long inicio = System.nanoTime();
        try (FileInputStream flujo = new FileInputStream(ruta)) {
            while (flujo.read() != -1) {
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("FileInputStream: " + tiempo + " ms");
    }
    private static void leerBufferedInputStream(String ruta) throws IOException {
        long inicio = System.nanoTime();
        try (BufferedInputStream flujo = new BufferedInputStream(new FileInputStream(ruta))) {
            while (flujo.read() != -1) {
            }
        }
        double tiempo = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.println("BufferedInputStream: " + tiempo + " ms");
    }
}

