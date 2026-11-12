import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConcurrenciaClient {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        try (Socket socket = new Socket(host, puerto)) {
            System.out.println("Conexion exitosa " + host + ":" + puerto);

            PrintWriter enviar = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader recibir = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // NUevo hilo que escucha al servidor e imprime lo que llegue
            Thread escucha = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = recibir.readLine()) != null) {
                        System.out.println(linea);
                    }
                } catch (IOException e) {
                    // el socket se cerro, no hacemos nada
                }
                System.out.println("Desconectado del servidor.");
                System.exit(0);
            });
            escucha.start();

            // Hilo principal: lee el teclado y envía
            String input;
            while ((input = br.readLine()) != null) {
                enviar.println(input);
                if (input.equalsIgnoreCase("exit()")) break;
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}