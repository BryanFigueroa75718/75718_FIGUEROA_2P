import java.io.*;
import java.net.Socket;

public class ConcurrentClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String respuestaServidor;
            while ((respuestaServidor = in.readLine()) != null) {
                if (respuestaServidor.startsWith("LOGIN:") || respuestaServidor.startsWith("ERROR:")) {
                    System.out.print(respuestaServidor.substring(respuestaServidor.indexOf(':') + 1) + " ");
                    String usuario = teclado.readLine();
                    if (usuario == null) return;
                    out.println(usuario);
                } else if (respuestaServidor.startsWith("OK:")) {
                    System.out.println("--------------------------------------------");
                    System.out.println(respuestaServidor.substring(3));
                    System.out.println("--------------------------------------------");
                    break;
                }
            }

            Thread hiloEscucha = new Thread(() -> {
                try {
                    String mensajeEntrante;
                    while ((mensajeEntrante = in.readLine()) != null) {
                        System.out.println(mensajeEntrante);
                    }
                    System.out.println("\n[Desconectado del servidor]");
                    System.exit(0);
                } catch (IOException e) {
                }
            });
            hiloEscucha.setDaemon(true);
            hiloEscucha.start();

            String mensajeUsuario;
            while ((mensajeUsuario = teclado.readLine()) != null) {
                out.println(mensajeUsuario);
                if ("salir".equalsIgnoreCase(mensajeUsuario.trim())) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("No se pudo conectar con el servidor en " + HOST + ":" + PORT + " -> " + e.getMessage());
        }
    }
}
