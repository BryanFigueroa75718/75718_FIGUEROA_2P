import java.io.*;

public class EscrituraBuffered {

    private static boolean imprimir = true;

    public static void main(String[] args) {
        int n = 10_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        imprimir = false;

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);

        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);

        imprimir = true;

        System.out.println("n = " + n);
        System.out.println("-------------------------------------------");

        System.out.print("TEXTO\n");

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);

        System.out.print("BINARIO\n");

        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        long tiempoInicial = System.nanoTime();

        try (FileWriter escritor = new FileWriter(ruta)){
            for (int i = 0; i < n; i++) {
                escritor.write("x");   
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        
        medirTiempo("FileWritter", n, tiempoInicial);
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        long tiempoInicial = System.nanoTime();
        try (BufferedWriter escritorBuffer = new BufferedWriter(new FileWriter(ruta))) {
            for (int i = 0; i < n; i++) {
                escritorBuffer.write("x");   
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        medirTiempo("BufferedWriter", n, tiempoInicial);
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        long tiempoInicial = System.nanoTime();
        long leidos = 0;

        try (FileReader escritorArchivos = new FileReader(ruta)) {
            while (escritorArchivos.read() != -1){
                leidos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        medirTiempo("FileReader", leidos, tiempoInicial);
    }

    private static void leerBufferedReader(String ruta) {
        long tiempoInicial = System.nanoTime();
        long leidos = 0;

        try (BufferedReader lectorBuffer = new BufferedReader(new FileReader(ruta))){
            while (lectorBuffer.read() != -1){
                leidos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        medirTiempo("BufferReader", leidos, tiempoInicial);
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        long tiempoInicial = System.nanoTime();

        try (FileOutputStream salidaArchivo = new FileOutputStream(ruta)){

            for(int i = 0; i < n; i++){
                salidaArchivo.write(i % 256);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        medirTiempo("FileOutputStream", n, tiempoInicial);
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        long tiempoInicial = System.nanoTime();

        try (BufferedOutputStream salidaBuffer = new BufferedOutputStream(new FileOutputStream(ruta))){
            for (int i = 0; i < n; i++){
                salidaBuffer.write( i % 256);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        medirTiempo("BufferedOutputStream", n, tiempoInicial);
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        long tiempoInicial = System.nanoTime();
        long leidos = 0;
        try (FileInputStream entradaArchivo = new FileInputStream(ruta)) {
            while (entradaArchivo.read() != -1) {
                leidos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        medirTiempo("FileInputStream", leidos, tiempoInicial);
    }

    private static void leerBufferedInputStream(String ruta) {
        long tiempoInicial = System.nanoTime();
        long leidos = 0;
        try (BufferedInputStream flujoEntrada = new BufferedInputStream(new FileInputStream(ruta))) {
            while (flujoEntrada.read() != -1) {
                leidos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        medirTiempo("BufferedInputStream", leidos, tiempoInicial);
    }

    //Para medir el tiempo

    private static void medirTiempo(String nombre, long elementos, long tiempoInicial){
        long tiempoFinal = System.nanoTime();
        double tiempoTotal = (tiempoFinal - tiempoInicial) / 1_000_000.0;
        if (imprimir){
            System.out.println(nombre + " " + elementos  + " " + tiempoTotal + " ms");
        }
    }
}


