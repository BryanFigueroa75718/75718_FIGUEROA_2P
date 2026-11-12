import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcurrenciaServer {
    public static void main(String[] args ){
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Starting server...");

            while (true){
                Socket client = serverSocket.accept();
                System.out.println(client.getInetAddress().getHostName());
                new Thread(()-> handleRequest(client)).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRequest(Socket clientSocket){
        try(
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)

        ){
//            String msj = in.readLine();
            String msj;

            while ((msj = in.readLine()) != null){

                System.out.println("[SERVIDOR]- Recibi: " + msj );
            }
        }catch (IOException e){}
    }
}
