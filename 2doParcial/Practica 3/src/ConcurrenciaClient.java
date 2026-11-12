import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class ConcurrenciaClient {
    public static void main(String[] args){
        String host = "localhost";
        int puerto = 8080;


        try (Socket socket = new Socket(host,puerto)) {
            System.out.println("Conexion exitosa " + host + ":" + puerto);

            PrintWriter enviar = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader recibir = new BufferedReader((new InputStreamReader(socket.getInputStream())));

            Reader entradaCliente = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(entradaCliente);

            String input;
            do {

                input = null;
                input = br.readLine();

                enviar.println(input);

            } while (!input.equalsIgnoreCase("exit()"));

        } catch (UnknownHostException e){
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
