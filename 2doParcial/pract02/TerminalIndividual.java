```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class TerminalIndividual {

    //estos son los colores que uso para personalizar la terminal
    static final String RESET = "\033[0m";
    static final String VERDE = "\033[32m";
    static final String CYAN = "\033[36m";
    static final String ROJO = "\033[31m";
    static final String NEGRITA = "\033[1m";
    static final String FONDO = "\033[44m";

    //estos son mis datos
    static final String MATERIA = "Concurrencia y Paralelismo 5A";
    static final String ALUMNO = "Maria F Suarez Arias";
    static final String MATRICULA = "AL069840";

    //esta expresion regular valida el nombre de usuario
    static final Pattern REGEX_USUARIO =
            Pattern.compile("^[A-Za-z][A-Za-z0-9_]{3,15}$");

    //este es el usuario que aparece al iniciar la terminal
    static String usuario = "invitado";

    public static void main(String[] args) {
        ejecutarTerminal();
    }

    //aqui se leen los comandos y se ejecutan
    public static void ejecutarTerminal() {
        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(System.in))) {

            String comando;

            //este es el encabezado de mi terminal
            System.out.println(CYAN + NEGRITA
                    + "================================"
                    + "\n     TERMINAL PERSONALIZADA"
                    + "\n================================"
                    + RESET);

            System.out.println("Escribe help para ver los comandos.");

            do {
                //esto muestra el usuario actual
                System.out.print(FONDO + " " + usuario + " " + RESET
                        + VERDE + " $ " + RESET);

                comando = lector.readLine();

                if (comando == null) {
                    break;
                }

                comando = comando.trim();

                //aqui reviso que comando escribio el usuario
                if (comando.equalsIgnoreCase("help")) {
                    mostrarAyuda();

                } else if (comando.equalsIgnoreCase("login")) {
                    iniciarSesion(lector);

                } else if (comando.equalsIgnoreCase("parImpar")) {
                    verificarParImpar(lector);

                } else if (comando.equalsIgnoreCase("passwordCheck")) {
                    comprobarPassword(lector);

                } else if (comando.equalsIgnoreCase("exit()")) {
                    break;

                } else if (!comando.isEmpty()) {
                    System.out.println(ROJO
                            + "Comando desconocido. Escribe help."
                            + RESET);
                }

            } while (true);

            //este mensaje aparece cuando cierro la terminal
            System.out.println(CYAN + "Hasta luego, "
                    + usuario + "." + RESET);

        } catch (IOException e) {
            System.out.println(ROJO
                    + "Ocurrio un error al leer los datos."
                    + RESET);
        }
    }

    //este comando muestra la ayuda y mis datos
    public static void mostrarAyuda() {
        System.out.println(CYAN + NEGRITA
                + "\n------ AYUDA ------" + RESET);

        System.out.println("Materia: " + MATERIA);
        System.out.println("Alumna: " + ALUMNO);
        System.out.println("Matricula: " + MATRICULA);

        System.out.println("\nComandos disponibles:");
        System.out.println(VERDE + "help" + RESET
                + " - Muestra la ayuda");
        System.out.println(VERDE + "login" + RESET
                + " - Valida el usuario");
        System.out.println(VERDE + "parImpar" + RESET
                + " - Dice si un numero es par o impar");
        System.out.println(VERDE + "passwordCheck" + RESET
                + " - Revisa la longitud de una contraseña");
        System.out.println(VERDE + "exit()" + RESET
                + " - Cierra la terminal");
    }

    //este comando revisa si el usuario cumple con el regex
    public static void iniciarSesion(BufferedReader lector)
            throws IOException {

        System.out.print("Ingresa tu nombre de usuario: ");
        String nuevoUsuario = lector.readLine();

        if (nuevoUsuario == null) {
            System.out.println(ROJO
                    + "Inicio de sesion cancelado." + RESET);
            return;
        }

        nuevoUsuario = nuevoUsuario.trim();

        //si el usuario cumple las reglas, inicio la sesion
        if (REGEX_USUARIO.matcher(nuevoUsuario).matches()) {
            usuario = nuevoUsuario;

            System.out.println(VERDE
                    + "Bienvenida, " + usuario + "!" + RESET);
        } else {
            System.out.println(ROJO
                    + "Usuario invalido. Debe tener de 4 a 16 caracteres,"
                    + " iniciar con una letra y usar letras, numeros"
                    + " o guion bajo."
                    + RESET);
        }
    }

    //este comando dice si un numero es par o impar
    public static void verificarParImpar(BufferedReader lector)
            throws IOException {

        try {
            System.out.print("Ingresa un numero entero: ");
            int numero = Integer.parseInt(lector.readLine());

            if (numero % 2 == 0) {
                System.out.println(VERDE
                        + numero + " es un numero par."
                        + RESET);
            } else {
                System.out.println(CYAN
                        + numero + " es un numero impar."
                        + RESET);
            }

        } catch (NumberFormatException e) {
            System.out.println(ROJO
                    + "Debes ingresar un numero entero."
                    + RESET);
        }
    }

    //este comando revisa si la contraseña tiene 8 caracteres
    public static void comprobarPassword(BufferedReader lector)
            throws IOException {

        System.out.print("Ingresa una contraseña para revisarla: ");
        String password = lector.readLine();

        if (password == null) {
            System.out.println(ROJO
                    + "Operacion cancelada." + RESET);
            return;
        }

        //aqui reviso la longitud de la contraseña
        if (password.length() >= 8) {
            System.out.println(VERDE
                    + "La contraseña cumple con la longitud minima."
                    + RESET);
        } else {
            System.out.println(ROJO
                    + "La contraseña debe tener al menos 8 caracteres."
                    + RESET);
        }
    }
}
```