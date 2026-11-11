import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        //cantidad de elementos, se puede pasar otro valor al correr
        //ejemplo: java EscrituraBuffered 10000000
        int n = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        //texto: primero escribe y luego lee, con y sin buffer
        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);

        //binario: igual, escribe y luego lee, con y sin buffer
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    //este metodo imprime el tiempo en ms para no repetir el mismo println en todos lados
    private static void mostrarTiempo(String nombre, long inicio) {
        long fin = System.nanoTime();
        double ms = (fin - inicio) / 1_000_000.0;
        System.out.println(nombre + ": " + ms + " ms");
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        //try-with-resources para que cierre el archivo solito
        try (FileWriter fw = new FileWriter(ruta)) {
            long inicio = System.nanoTime(); //empieza a contar aqui

            for (int i = 0; i < n; i++) {
                //cada write va directo al disco, por eso es lento
                fw.write("Hello World" + i + "\n");
            }

            fw.flush(); //manda lo que falte al disco antes de medir
            mostrarTiempo("FileWriter", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        //el BufferedWriter envuelve al FileWriter
        try (FileWriter fw = new FileWriter(ruta);
             BufferedWriter bw = new BufferedWriter(fw)) {
            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                //ojo: aqui se escribe con bw y no con fw, si no el buffer no sirve
                bw.write("Hello World" + i + "\n");
            }

            bw.flush(); //vacia el buffer al disco, esto tambien cuenta en el tiempo
            mostrarTiempo("BufferedWriter", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        try (FileReader fr = new FileReader(ruta)) {
            long inicio = System.nanoTime();

            int caracter;
            long total = 0; //cuenta los caracteres leidos
            //read() regresa -1 cuando ya no hay nada que leer
            while ((caracter = fr.read()) != -1) {
                total++;
            }

            mostrarTiempo("FileReader (" + total + " caracteres)", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leerBufferedReader(String ruta) {
        try (FileReader fr = new FileReader(ruta);
             BufferedReader br = new BufferedReader(fr)) {
            long inicio = System.nanoTime();

            int caracter;
            long total = 0;
            //lo mismo que arriba pero con br, asi la comparacion es justa
            while ((caracter = br.read()) != -1) {
                total++;
            }

            mostrarTiempo("BufferedReader (" + total + " caracteres)", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        try (FileOutputStream fos = new FileOutputStream(ruta)) {
            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                //write(int) solo guarda el byte de abajo, por eso se repiten del 0 al 255
                fos.write(i);
            }

            mostrarTiempo("FileOutputStream", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        try (FileOutputStream fos = new FileOutputStream(ruta);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            long inicio = System.nanoTime();

            for (int i = 0; i < n; i++) {
                //aqui si va con bos, junta bytes y los manda por bloques de 8 KB
                bos.write(i);
            }

            bos.flush(); //vacia lo que quedo en el buffer
            mostrarTiempo("BufferedOutputStream", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        try (FileInputStream fis = new FileInputStream(ruta)) {
            long inicio = System.nanoTime();

            int octeto;
            long total = 0; //cuenta los bytes leidos
            //se revisa el -1 antes de usar el byte, asi no se cuela un -1 de mas
            while ((octeto = fis.read()) != -1) {
                total++;
            }

            mostrarTiempo("FileInputStream (" + total + " bytes)", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leerBufferedInputStream(String ruta) {
        try (FileInputStream fis = new FileInputStream(ruta);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            long inicio = System.nanoTime();

            int octeto;
            long total = 0;
            //igual que el de arriba pero leyendo del buffer
            while ((octeto = bis.read()) != -1) {
                total++;
            }

            mostrarTiempo("BufferedInputStream (" + total + " bytes)", inicio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
