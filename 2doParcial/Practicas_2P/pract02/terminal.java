//Gladys Candy Chumá Pérez - 70238
package pract02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class terminal {

    public static void main(String[] args) {

        // Permite escribir los comandos desde la consola
        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(System.in))) {

            String comando;
            String usuario = "";
            boolean sesionIniciada = false;

            // Le ponemos un nombre y una pequeña bienvenida a la terminal
            System.out.println("▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️");
            System.out.println("         MINI TERMINAL        ");
            System.out.println("️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫️▫▫");
            System.out.println("Bienvenido!");
            System.out.println("Escribe login para iniciar sesión.");

            // Este ciclo hace que la terminal siga funcionando hasta que escribamos el comando para salir
            while (true) {

                // Si ya iniciamos sesion, aparece nuestro usuario y si no, se queda el nombre de la terminal
                if (sesionIniciada) {
                    System.out.print("\n" + usuario + " > ");
                } else {
                    System.out.print("\nterminal > ");
                }

                comando = lector.readLine();

                // Si ya no hay nada que leer, termina el programa
                if (comando == null) {
                    break;
                }

                // Aqui revisamos cual comando escribimos
                switch (comando.trim().toLowerCase()) {

                    case "login":

                        // Evitamos iniciar sesion otra vez si ya entramos
                        if (sesionIniciada) {
                            System.out.println("Ya tienes una sesión iniciada.");
                            break;
                        }

                        System.out.print("Ingresa tu correo institucional: ");
                        String correo = lector.readLine();

                        if (correo == null) {
                            return;
                        }

                        // Revisamos que el correo tenga el formato
                        // que utiliza la universidad
                        if (correo.matches("al[0-9]{6}@uacam\\.mx")) {

                            // Guardamos solo la parte antes del arroba para mostrarla como nombre en la terminal
                            usuario = correo.substring(0, correo.indexOf("@"));
                            sesionIniciada = true;

                            System.out.println("Correo válido");
                            System.out.println("Bienvenido, " + usuario);
                            System.out.println("Escribe help para ver los comandos.");

                        } else {
                            System.out.println("Correo no válido.");
                        }
                        break;

                    case "help":

                        // Si no hemos iniciado sesion, no deja usar el comando
                        if (!sesionIniciada) {
                            System.out.println("Primero debes iniciar sesion con login.");
                            break;
                        }

                        // Mostramos los comandos que tenemos disponibles
                        System.out.println("\nComandos disponibles:");
                        System.out.println("help  - Ver los comandos");
                        System.out.println("fecha - Mostrar la fecha actual");
                        System.out.println("hora  - Mostrar la hora actual");
                        System.out.println("exit  - Cerrar la terminal");
                        break;

                    case "fecha":

                        if (!sesionIniciada) {
                            System.out.println("Primero debes iniciar sesión.");
                            break;
                        }

                        // Obtenemos la fecha de la y la acomodamos para que se vea dia/mes/año
                        LocalDate fecha = LocalDate.now();
                        DateTimeFormatter formatoFecha =
                                DateTimeFormatter.ofPattern("dd/MM/yyyy");

                        System.out.println("Fecha actual 📆: "
                                + fecha.format(formatoFecha));
                        break;

                    case "hora":

                        if (!sesionIniciada) {
                            System.out.println("Primero debes iniciar sesion.");
                            break;
                        }

                        // Muestra la hora actual
                        LocalTime hora = LocalTime.now();
                        DateTimeFormatter formatoHora =
                                DateTimeFormatter.ofPattern("HH:mm:ss");

                        System.out.println("Hora actual 🕛: "
                                + hora.format(formatoHora));
                        break;

                    case "exit":

                        // Cerramos la terminal cuando escribimos exit
                        System.out.println("Cerrando terminal 👋 ...");
                        return;

                    case "":

                        // Si solo damos Enter, no hace nada
                        break;

                    default:

                        // Esto aparece cuando escribimos algo que no sea uno de nuestros comandos
                        if (!sesionIniciada) {
                            System.out.println("Primero debes iniciar sesion con login.");
                        } else {
                            System.out.println("Comando no reconocido.");
                            System.out.println("Escribe help para ver las opciones.");
                        }
                        break;
                }
            }

        } catch (IOException e) {

            // Mostramos un mensaje si ocurre algun error al leer
            System.out.println("Error: " + e.getMessage());
        }
    }
}
