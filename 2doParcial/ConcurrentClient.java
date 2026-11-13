import java.io.*;
import java.net.*;

public class ConcurrentClient {
    public static void main(String[] args) {
        String host = "10.5.84.172";
        int puerto = 8080;

        try (Socket socket = new Socket(host, puerto)) {
            System.out.println("Conexión exitosa: " + host + ":" + puerto);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader((new InputStreamReader(socket.getInputStream())));

            output.println("69875 Angela Alicia PImienta Diaz uwu");

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}