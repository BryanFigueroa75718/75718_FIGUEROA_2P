import java.io.*;
import java.util.Scanner;

public class EstructuraBuffer {

    public static void main(String[] args) {
        int n = 10_000_000;
        String rutaTexto = "archivoSalida.txt";
        String rutaBinario = "archivoSalida.dat";
        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerBufferedInputStream(rutaBinario);
        leerFileInputStream(rutaBinario);
 /*        int bandera = 0;
        Scanner scan = new Scanner(System.in);

        while (bandera != 1) {
            System.out.println("====== Opciones A Operar ======\n");
            System.out.println("1) escribirFileWriter");
            System.out.println("2) escribirBufferedWriter");
            System.out.println("3) leerFileReader");
            System.out.println("4) leerBufferedReader");
            System.out.println("5) escribirFileOutputStream");
            System.out.println("6) escribirBufferedOutputStream");
            System.out.println("7) leerFileInputStream");
            System.out.println("8) leerBufferedInputStream");
            System.out.println("9) Salir \n");
            System.out.print("Elige una opción: ");

            int select = scan.nextInt();

            switch (select) {
                case 1:
                    escribirFileWriter(rutaTexto, n);
                    break;
                case 2:
                    escribirBufferedWriter(rutaTexto, n);
                    break;
                case 3:
                    leerFileReader(rutaTexto);
                    break;
                case 4:
                    leerBufferedReader(rutaTexto);
                    break;
                case 5:
                    escribirFileOutputStream(rutaBinario, n);
                    break;
                case 6:
                    escribirBufferedOutputStream(rutaBinario, n);
                    break;
                case 7:
                    leerFileInputStream(rutaBinario);
                    break;
                case 8:
                    leerBufferedInputStream(rutaBinario);
                    break;
                case 9:
                    bandera = 1;
                    System.out.println("¡Adiós! :)");
                    break;
                default:
                    System.out.println("Número ingresado no válido.\n");
                    break;
            }
        }
        scan.close(); */
    }

    public static void escribirFileWriter(String ruta, int n) {
        try (FileWriter fw = new FileWriter(ruta)) {
            long initTime = System.nanoTime();

            for (int i = 0; i < n; i++) {
                fw.write("HOla Soy Benja wt " + i + "\n");
            }

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Escribir FileWriter: " + (executing / 1_000_000) + " ms\n");

        } catch (Exception e) {
            System.out.println("Tenemos un error: " + e);
        }
    }

    public static void escribirBufferedWriter(String ruta, int n) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            long initTime = System.nanoTime();

            for (int i = 0; i < n; i++) {
                bw.write("hola soy Benja bt " + i + "\n");
            }

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Escribir BufferedWriter: " + (executing / 1_000_000) + " ms\n");

        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
    }

    public static void leerFileReader(String ruta) {
        try (FileReader fr = new FileReader(ruta)) {
            long initTime = System.nanoTime();

            long totalCaracteres = 0;
            int valor = fr.read();
            while (valor != -1) {
                totalCaracteres++;
                valor = fr.read();
            }

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Leer FileReader (Caracter por caracter): " + (executing / 1_000_000) + " ms [" + totalCaracteres + " caracteres]\n");

        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
    }

    public static void leerBufferedReader(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            long initTime = System.nanoTime();

            long totalLineas = 0;
            String linea = br.readLine();
            while (linea != null) {
                totalLineas++;
                linea = br.readLine();
            }

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Leer BufferedReader (Por líneas): " + (executing / 1_000_000) + " ms [" + totalLineas + " líneas]\n");

        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
    }

    public static void escribirFileOutputStream(String ruta, int n) {
        try (FileOutputStream fo = new FileOutputStream(ruta)) {
            String texto = "Hola soy benja fo \n";
            byte[] codigo = texto.getBytes();

            long initTime = System.nanoTime();
            
            for (int i = 0; i < n; i++) {
                fo.write(codigo);
            }
            fo.flush();

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Escribir FileOutputStream: " + (executing / 1_000_000) + " ms\n");

        } catch (Exception e) {
            System.out.println("Error en FileOutputStream: " + e);
        }
    }

    public static void escribirBufferedOutputStream(String ruta, int n) {
        try (BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(ruta))) {
            long initTime = System.nanoTime();
            
            for (int i = 0; i < n; i++) {
                String ms = "hola Soy Benja bo " + i + "\n";
                byte[] codigo = ms.getBytes();
                bo.write(codigo);
            }
            bo.flush();

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Escribir BufferedOutputStream: " + (executing / 1_000_000) + " ms\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void leerFileInputStream(String ruta) {
        try (FileInputStream fi = new FileInputStream(ruta)) {
            long initTime = System.nanoTime();

            long totalBytes = 0;
            int valor = fi.read();
            while (valor != -1) {
                totalBytes++;
                valor = fi.read();
            }

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Leer FileInputStream (Byte por byte): " + (executing / 1_000_000) + " ms [" + totalBytes + " bytes]\n");

        } catch (IOException e) {
            System.out.println("Error en FileInputStream: " + e);
        }
    }

    public static void leerBufferedInputStream(String ruta) {
        try (BufferedInputStream bi = new BufferedInputStream(new FileInputStream(ruta))) {
            long initTime = System.nanoTime();

            long totalBytes = 0;
            int valor = bi.read();
            while (valor != -1) {
                totalBytes++;
                valor = bi.read();
            }

            long endTime = System.nanoTime();
            long executing = endTime - initTime;
            System.out.println("-> Leer BufferedInputStream (Byte por byte): " + (executing / 1_000_000) + " ms [" + totalBytes + " bytes]\n");

        } catch (Exception e) {
            System.out.println("Error en BufferedInputStream: " + e);
        }
    }
}