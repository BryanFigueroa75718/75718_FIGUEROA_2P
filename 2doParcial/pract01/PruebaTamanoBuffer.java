import java.io.*;

/**
 * Experimento complementario (pregunta 4): efecto del tamaño del buffer
 * en BufferedOutputStream / BufferedInputStream y BufferedWriter / BufferedReader.
 
 */
public class PruebaTamanoBuffer {
    static long sumidero = 0;

    public static void main(String[] args) throws IOException {
        int n = (args.length > 0) ? Integer.parseInt(args[0]) : 10_000_000;
        int[] tamanos = {16, 128, 1024, 8192, 65536};
        // calentamiento
        for (int t : tamanos) { escribirBin("tmp.dat", n / 10, t); leerBin("tmp.dat", t); }
        for (int t : tamanos) {
            double wb = escribirBin("tmp.dat", n, t);
            double rb = leerBin("tmp.dat", t);
            double wt = escribirTxt("tmp.txt", n / 10, t);
            double rt = leerTxt("tmp.txt", t);
            System.out.printf(java.util.Locale.ROOT, "BUF;%d;%.3f;%.3f;%.3f;%.3f%n", t, wb, rb, wt, rt);
        }
        new File("tmp.dat").delete();
        new File("tmp.txt").delete();
    }

    static double escribirBin(String ruta, int n, int tam) throws IOException {
        long t0 = System.nanoTime();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(ruta), tam)) {
            for (int i = 0; i < n; i++) bos.write(i);
        }
        return (System.nanoTime() - t0) / 1e6;
    }

    static double leerBin(String ruta, int tam) throws IOException {
        long t0 = System.nanoTime(); long s = 0; int b;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(ruta), tam)) {
            while ((b = bis.read()) != -1) s += b;
        }
        sumidero += s;
        return (System.nanoTime() - t0) / 1e6;
    }

    static double escribirTxt(String ruta, int n, int tam) throws IOException {
        long t0 = System.nanoTime();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta), tam)) {
            for (int i = 0; i < n; i++) { bw.write("Hello World" + i); bw.newLine(); }
        }
        return (System.nanoTime() - t0) / 1e6;
    }

    static double leerTxt(String ruta, int tam) throws IOException {
        long t0 = System.nanoTime(); String l;
        try (BufferedReader br = new BufferedReader(new FileReader(ruta), tam)) {
            while ((l = br.readLine()) != null) sumidero += l.length();
        }
        return (System.nanoTime() - t0) / 1e6;
    }
}
