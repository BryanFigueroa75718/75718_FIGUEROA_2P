package pract01;

import java.io.*;

public class Act_Stream {

    public static void main(String[] args) {
        escribirCaracteres();
    }

    public static void escribirCaracteres() {
        try (Writer w = new FileWriter("pract01/info.txt");
            BufferedWriter bw = new BufferedWriter(w)
        ){
            for (int i = 0; i < 1_000; i++) {
                w.write("Hello World" + i +"\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void escribir( ) {

      try (OutputStream fos = new FileOutputStream("pract01/data.txt");
           ObjectOutputStream oos = new ObjectOutputStream(fos);
      ) {
        oos.writeUTF("Hello World");
      } catch (Exception e) {
            throw new RuntimeException(e);
      }
        
    }
    public static void escribir_mucho( ) {

      try (OutputStream fos = new FileOutputStream("pract01/data.txt");
           //Probar con la clase Object.
           //ObjectOutputStream oos = new ObjectOutputStream(fos);
           BufferedOutputStream bos = new BufferedOutputStream(fos);
      ) {
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

    
    public static void leer()  {
        //try with
       try (FileInputStream fis = new FileInputStream("pract01/data.txt");
            BufferedInputStream bis = new BufferedInputStream(fis)
       ){
           int octeto = 0;
           while (octeto != -1){
               octeto = bis.read();
               if (octeto%30 == 0) {
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

//    public static void guardarNota (String nota) {
//        System.out.println("guardar Nota");
//    }
//
//    public static String leerNotas() {
//        return "notas";
//    }
}
