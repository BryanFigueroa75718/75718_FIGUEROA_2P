import java.io.*;

public static void leerCaracteres() {
    // Colores ANSI
    String RESET = "\u001B[0m";
    String ROJO = "\u001B[31m";
    String VERDE = "\u001B[32m";
    String AMARILLO = "\u001B[33m";
    String AZUL = "\u001B[34m";
    String MORADO = "\u001B[35m";

    String usuario = "invitado";
    String color = VERDE;

    try (Reader r = new InputStreamReader(System.in);
         BufferedReader br = new BufferedReader(r)) {
        String ch;
        System.out.println(MORADO + "👋 Bienvenido al chat, escribe el comando 'help' para ver en lo que te puedo ayudar... " + RESET);
        do {
            System.out.print(color + "👤 " + usuario + " > " + RESET);
            ch = br.readLine();
            if (ch != null && !ch.equalsIgnoreCase("exit()")) {

                if (ch.equalsIgnoreCase("help")) {
                    System.out.println(AMARILLO + "📋 Comandos que se pueden solicitar:" + RESET);
                    System.out.println("  help             ---> Lista de todos los comandos");
                    System.out.println("  login <correo>   ---> Iniciar sesión (ej. NombreUsuario@uacam.mx)");
                    System.out.println("  hora             ---> Muestra la hora actual");
                    System.out.println("  contar <texto>   ---> Cuenta los caracteres de un texto");
                    System.out.println("  color <nombre>   ---> Cambia el color (rojo, verde, azul)");
                    System.out.println("  exit()           ---> Salir");

                } else if (ch.startsWith("login ")) {
                    String correo = ch.substring(6).trim();
                    if (correo.matches("^[a-zA-Z0-9._-]+@uacam\\.mx$")) {
                        usuario = correo;
                        System.out.println(VERDE + "✅ Bienvenido " + correo + RESET);
                    } else {
                        System.out.println(ROJO + "❌ Correo inválido, debe ser usuario@uacam.mx" + RESET);
                    }

                } else if (ch.equalsIgnoreCase("hora")) {
                    System.out.println(AZUL + "🕒 Son las " + java.time.LocalTime.now().withNano(0) + RESET);

                } else if (ch.startsWith("contar ")) {
                    System.out.println(AZUL + "🔢 > comando " + ch.substring(7).length() + RESET);

                } else if (ch.startsWith("color ")) {
                    String nombre = ch.substring(6).trim().toLowerCase();
                    if (nombre.equals("rojo")) color = ROJO;
                    else if (nombre.equals("verde")) color = VERDE;
                    else if (nombre.equals("azul")) color = AZUL;
                    else System.out.println(ROJO + "❌ Color no válido, solo: rojo, verde, azul" + RESET);

                } else {
                    System.out.println(ROJO + "⚠️ Comando no encontrado, escribe 'help'" + RESET);
                }
            }
        } while (ch != null && !ch.equalsIgnoreCase("exit()"));
        System.out.println(MORADO + "👋 Hasta luego" + RESET);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

void main() {
    leerCaracteres();
}