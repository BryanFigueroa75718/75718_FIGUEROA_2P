
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.print.DocFlavor.READER;

public class Act_Stream {

    public static void main(String[] args) {
        escribirCaracteres();
        leerCaracteres();
    }

    public static void leerCaracteres() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String ch;
            
            do {
               System.out.println("======== Eligir una opcio n=========");
                System.out.println("1) saber la hora");
                System.out.println("2) saber fecha");
                System.out.println("exit()) para salir \n");
                System.out.print("terminalBenja> ");
                 ch = br.readLine();
                if (ch != null && !ch.equalsIgnoreCase("exit()")) {

                    System.out.println("> comando " + ch.length());
                    switch (ch) {
                        case "1":
                            hora();
                            break;
                        case "2":
                            fecha();
                            break;
                        default:
                            System.out.println("valor no valido");
                            break;
                    }
                }

            } while (ch != null && !ch.equalsIgnoreCase("exit()"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hora() {
        LocalTime hora = LocalTime.now();
        System.out.println(">  la Hora actual: " + hora + "\n");
    }

    public static void fecha() {
        LocalDate fecha = LocalDate.now();
        System.out.println("> la fecha es: " + fecha + "\n");
    }

    public static int multi(int numero1) {
        int Resultado = numero1 * 2;
        return Resultado;
    }

    public static void escribirCaracteres() {
        try (Writer w = new FileWriter("info.txt");
                BufferedWriter bw = new BufferedWriter(w)) {
            for (int i = 0; i < 1_000; i++) {
                w.write("Hello World" + i + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void escribir() {

        try (OutputStream fos = new FileOutputStream("data.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeUTF("Hello World");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void escribir_mucho() {

        try (OutputStream fos = new FileOutputStream("data.txt");
                // Probar con la clase Object.
                // ObjectOutputStream oos = new ObjectOutputStream(fos);
                BufferedOutputStream bos = new BufferedOutputStream(fos);) {
            long inicio = System.nanoTime();

            for (int i = 0; i < 1_000_000; i++) {
                fos.write(i);
            }

            long fin = System.nanoTime();
            long duracionNs = fin - inicio;
            double duracionMs = duracionNs / 1_000_000.0;
            System.out.println("Duración: " + duracionNs + " ns (" + duracionMs + " ms)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void leer() {
        // try with
        try (FileInputStream fis = new FileInputStream("data.txt");
                BufferedInputStream bis = new BufferedInputStream(fis)) {
            int octeto = 0;
            while (octeto != -1) {
                octeto = bis.read();
                if (octeto % 30 == 0) {
                    System.out.println();
                }
                System.out.print(octeto + " ");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // public static void guardarNota (String nota) {
    // System.out.println("guardar Nota");
    // }
    //
    // public static String leerNotas() {
    // return "notas";
    // }
}
