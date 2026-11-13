import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConcurrentClient {

    private static final String TARGET_HOST = "localhost";
    private static final int TARGET_PORT = 8080;

    public static void main(String[] executionArguments) {
        try (
            Socket netSocket = new Socket(TARGET_HOST, TARGET_PORT);
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(netSocket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(netSocket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))
        ) {
            // --- ETAPA INICIAL DE AUTENTICACIÓN ---
            if (!procesarAutenticacion(socketReader, socketWriter, consoleReader)) {
                return;
            }

            // --- ETAPA DE ESCUCHA PARALELA (Background Reader) ---
            activarEscuchaAsincrona(socketReader);

            // --- ETAPA DE TRANSMISIÓN PRINCIPAL (Main Thread Writer) ---
            procesarEnvioConsola(socketWriter, consoleReader);

        } catch (IOException ex) {
            System.err.println("Falla crítica al conectar con el nodo en " + TARGET_HOST + ":" + TARGET_PORT + " -> " + ex.getMessage());
        }
    }

    private static boolean procesarAutenticacion(BufferedReader reader, PrintWriter writer, BufferedReader console) throws IOException {
        String serverResponse;
        while ((serverResponse = reader.readLine()) != null) {
            if (serverResponse.startsWith("LOGIN:") || serverResponse.startsWith("ERROR:")) {
                System.out.print(serverResponse.substring(serverResponse.indexOf(':') + 1) + " ");
                String credentialInput = console.readLine();
                if (credentialInput == null) {
                    return false;
                }
                writer.println(credentialInput);
            } else if (serverResponse.startsWith("OK:")) {
                System.out.println("--------------------------------------------------");
                System.out.println(serverResponse.substring(3));
                System.out.println("--------------------------------------------------");
                return true;
            }
        }
        return false;
    }

    private static void activarEscuchaAsincrona(BufferedReader reader) {
        Thread backgroundReceiver = new Thread(() -> {
            try {
                String incomingStreamData;
                while ((incomingStreamData = reader.readLine()) != null) {
                    System.out.println(incomingStreamData);
                }
                System.out.println("\n[Conexión finalizada por el host remoto]");
                System.exit(0);
            } catch (IOException ex) {
                // Excepción controlada al cerrar el socket de forma limpia con 'salir'
            }
        });
        backgroundReceiver.setDaemon(true);
        backgroundReceiver.start();
    }

    private static void procesarEnvioConsola(PrintWriter writer, BufferedReader console) throws IOException {
        String outboundPayload;
        while ((outboundPayload = console.readLine()) != null) {
            writer.println(outboundPayload);
            if ("salir".equalsIgnoreCase(outboundPayload.trim())) {
                break;
            }
        }
    }
}