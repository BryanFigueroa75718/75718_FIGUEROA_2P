package Practica06_Aaron;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;



public class main {
    public static void main(String[] args) {
      int n = 1_000_000;
    String rutaTexto = "archivo_salida.txt";
    String rutaBinario = "archivo_salida.txt";
         System.out.println("===== ESCRITURA DE TEXTO =====");
        EscrituraBuffered.escribirFileWriter(rutaTexto, n);
        EscrituraBuffered.escribirBufferedWriter(rutaTexto, n);

        System.out.println("\n===== LECTURA DE TEXTO =====");
        EscrituraBuffered.leerFileReader(rutaTexto);
        EscrituraBuffered.leerBufferedReader(rutaTexto);

        System.out.println("\n===== ESCRITURA BINARIA =====");
        EscrituraBuffered.escribirFileOutputStream(rutaBinario, n);
        EscrituraBuffered.escribirBufferedOutputStream(rutaBinario, n);

        System.out.println("\n===== LECTURA BINARIA =====");
        EscrituraBuffered.leerFileInputStream(rutaBinario);
        EscrituraBuffered.leerBufferedInputStream(rutaBinario);

    
      /*Act_Stream.escribir_mucho();
      try {
        Act_Stream.leer();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }*/

   // Act_Stream.escribirCaracteres();
   // -----Nueva tarea EscrituraBuffered-----


    
    }
    
}