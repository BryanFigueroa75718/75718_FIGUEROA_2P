// Laura Lucia Jaramillo Yah - 17/10/2026

import java.io.*; // importa las clases necesarias para trabajar con archivos

public class EscrituraBuffered { // crea la clase principal del programa

    public static void main(String[] args) { // aquí comienza la ejecución del programa
        int n = 10_000_000; // indica la cantidad de elementos que se van a procesar

        String rutaTexto = "archivo_salida.txt"; // guarda el nombre del archivo de texto
        String rutaBinario = "archivo_salida.dat"; // guarda el nombre del archivo binario

        System.out.println("- Escritura de texto -"); // muestra el título de la prueba
        escribirFileWriter(rutaTexto, n); // escribe texto sin utilizar buffer
        escribirBufferedWriter(rutaTexto, n); // escribe texto utilizando buffer

        System.out.println("\n- Lectura de texto -"); // muestra el título de la lectura de texto
        leerFileReader(rutaTexto); // lee el texto sin utilizar buffer
        leerBufferedReader(rutaTexto); // lee el texto utilizando buffer

        System.out.println("\n- Escritura binaria -"); // muestra el título de la escritura binaria
        escribirFileOutputStream(rutaBinario, n); // escribe bytes sin utilizar buffer
        escribirBufferedOutputStream(rutaBinario, n); // escribe bytes utilizando buffer

        System.out.println("\n- Lectura binaria -"); // muestra el título de la lectura binaria
        leerFileInputStream(rutaBinario); // lee bytes sin utilizar buffer
        leerBufferedInputStream(rutaBinario); // lee bytes utilizando buffer
    }

    // escritura de texto sin buffer
    private static void escribirFileWriter(String ruta, int n) { // recibe la ruta del archivo y la cantidad de elementos
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (FileWriter archivo = new FileWriter(ruta)) { // abre el archivo para escribir texto
            for (int i = 0; i < n; i++) { // repite el proceso hasta llegar a la cantidad indicada
                archivo.write("Hola mundo " + i + "\n"); // escribe un mensaje con el número actual y un salto de línea
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al escribir
            System.out.println("Error al escribir texto: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("FileWriter: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }

    // escritura de texto con buffer
    private static void escribirBufferedWriter(String ruta, int n) { // recibe la ruta del archivo y la cantidad de elementos
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (BufferedWriter archivo =
                     new BufferedWriter(new FileWriter(ruta))) { // abre el archivo con un buffer para escribir por bloques
            for (int i = 0; i < n; i++) { // repite el proceso la cantidad de veces indicada
                archivo.write("Hola mundo " + i); // escribe el mensaje con el número actual
                archivo.newLine(); // agrega un salto de línea
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al escribir
            System.out.println("Error al escribir texto: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("BufferedWriter: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }

    // lectura de texto sin buffer
    private static void leerFileReader(String ruta) { // recibe la ruta del archivo que se va a leer
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (FileReader archivo = new FileReader(ruta)) { // abre el archivo para leer caracteres
            while (archivo.read() != -1) { // lee un caracter en cada repetición hasta llegar al final del archivo
                // no se necesita hacer nada con el caracter porque solo se mide el tiempo de lectura
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al leer
            System.out.println("Error al leer texto: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("FileReader: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }

    // lectura de texto con buffer
    private static void leerBufferedReader(String ruta) { // recibe la ruta del archivo que se va a leer
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (BufferedReader archivo =
                     new BufferedReader(new FileReader(ruta))) { // abre el archivo para leer texto usando buffer
            while (archivo.readLine() != null) { // lee una línea completa hasta llegar al final del archivo
                // no se necesita hacer nada con la línea porque solo se mide el tiempo de lectura
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al leer
            System.out.println("Error al leer texto: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("BufferedReader: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }

    // escritura de bytes sin buffer
    private static void escribirFileOutputStream(String ruta, int n) { // recibe la ruta del archivo y la cantidad de bytes
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (FileOutputStream archivo = new FileOutputStream(ruta)) { // abre el archivo para escribir bytes
            for (int i = 0; i < n; i++) { // repite el proceso hasta llegar a la cantidad indicada
                archivo.write(i % 256); // escribe un byte con un valor entre 0 y 255
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al escribir
            System.out.println("Error al escribir bytes: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("FileOutputStream: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }

    // escritura de bytes con buffer
    private static void escribirBufferedOutputStream(String ruta, int n) { // recibe la ruta del archivo y la cantidad de bytes
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (BufferedOutputStream archivo =
                     new BufferedOutputStream(new FileOutputStream(ruta))) { // abre el archivo con un buffer para escribir por bloques
            for (int i = 0; i < n; i++) { // repite el proceso la cantidad de veces indicada
                archivo.write(i % 256); // escribe un byte con un valor entre 0 y 255
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al escribir
            System.out.println("Error al escribir bytes: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("BufferedOutputStream: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }

    // lectura de bytes sin buffer
    private static void leerFileInputStream(String ruta) { // recibe la ruta del archivo que se va a leer
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (FileInputStream archivo = new FileInputStream(ruta)) { // abre el archivo para leer bytes
            while (archivo.read() != -1) { // lee un byte en cada repetición hasta llegar al final del archivo
                // no se necesita guardar el byte porque solo se mide el tiempo de lectura
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al leer
            System.out.println("Error al leer bytes: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("FileInputStream: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }

    // lectura de bytes con buffer
    private static void leerBufferedInputStream(String ruta) { // recibe la ruta del archivo que se va a leer
        long inicio = System.nanoTime(); // guarda el tiempo inicial

        try (BufferedInputStream archivo =
                     new BufferedInputStream(new FileInputStream(ruta))) { // abre el archivo para leer bytes usando buffer
            while (archivo.read() != -1) { // lee un byte en cada repetición hasta llegar al final del archivo
                // no se necesita guardar el byte porque solo se mide el tiempo de lectura
            }
        } catch (IOException e) { // captura los errores que pueden ocurrir al leer
            System.out.println("Error al leer bytes: " + e.getMessage()); // muestra el motivo del error
            return; // termina este método si ocurre un error
        }

        long fin = System.nanoTime(); // guarda el tiempo final
        System.out.printf("BufferedInputStream: %.3f ms%n", // muestra el nombre del flujo y el tiempo con tres decimales
                (fin - inicio) / 1_000_000.0); // calcula el tiempo transcurrido y lo convierte a milisegundos
    }
}