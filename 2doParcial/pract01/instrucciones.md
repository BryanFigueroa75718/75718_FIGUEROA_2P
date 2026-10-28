# Práctica: Medición de tiempo de ejecución con flujos con y sin buffer

## Objetivo

Generar/leer archivos de texto y binarios en Java utilizando las clases `FileWriter`, `FileReader`, `BufferedWriter`, `BufferedReader`, `FileOutputStream`, `BufferedOutputStream`, `FileInputStream` y `BufferedInputStream`, y medir el tiempo de ejecución del proceso con `System.nanoTime()`.

---

## Marco teórico breve

### Flujos de caracteres (texto)
- **`FileWriter`**: escribe caracteres directamente al disco, operación costosa si se hace carácter por carácter.
- **`BufferedWriter`**: almacena los caracteres en un buffer interno y los escribe al disco en bloques, reduciendo accesos al sistema de archivos.
- **`FileReader`**: lee caracteres directamente desde el disco.
- **`BufferedReader`**: lee caracteres en bloques desde un buffer interno, reduciendo accesos al disco. Además provee `readLine()`.

### Flujos de bytes (binario)
- **`FileOutputStream`**: escribe bytes directamente al disco.
- **`BufferedOutputStream`**: almacena bytes en un buffer interno (por defecto 8 KB) y los escribe en bloques.
- **`FileInputStream`**: lee bytes directamente desde el disco.
- **`BufferedInputStream`**: lee bytes en bloques desde un buffer interno.

### Medición
- **`System.nanoTime()`**: retorna el tiempo actual en nanosegundos con alta precisión; se usa para medir intervalos.

---

## Instrucciones

### Paso 1. Crear el archivo Java

Crea un archivo llamado `EscrituraBuffered.java` en tu proyecto.

### Paso 2. Definir la cantidad de datos a escribir

Dentro del método `main`, declara una variable `n` con la cantidad de elementos a escribir. Por ejemplo:

```java
int n = 1_000_000; // 1 millón de elementos
```

> Puedes probar con distintos valores: 1_000, 100_000, 1_000_000, 10_000_000, etc.

### Paso 3. Definir las rutas de los archivos

```java
String rutaTexto   = "archivo_salida.txt";
String rutaBinario = "archivo_salida.dat";
```

### Paso 4. Código base de ejemplo

Utiliza el siguiente código como **base** y adáptalo para probar cada combinación (con y sin buffer, texto y binario):

```java
import java.io.*;

public class EscrituraBuffered {

    public static void main(String[] args) {
        int n = 1_000_000;
        String rutaTexto   = "archivo_salida.txt";
        String rutaBinario = "archivo_salida.dat";

        escribirFileWriter(rutaTexto, n);
        escribirBufferedWriter(rutaTexto, n);
        leerFileReader(rutaTexto);
        leerBufferedReader(rutaTexto);
        escribirFileOutputStream(rutaBinario, n);
        escribirBufferedOutputStream(rutaBinario, n);
        leerFileInputStream(rutaBinario);
        leerBufferedInputStream(rutaBinario);
    }

    // ---------- Texto: escritura ----------

    private static void escribirFileWriter(String ruta, int n) {
        // TODO
    }

    private static void escribirBufferedWriter(String ruta, int n) {
        // TODO
    }

    // ---------- Texto: lectura ----------

    private static void leerFileReader(String ruta) {
        // TODO
    }

    private static void leerBufferedReader(String ruta) {
        // TODO
    }

    // ---------- Binario: escritura ----------

    private static void escribirFileOutputStream(String ruta, int n) {
        // TODO
    }

    private static void escribirBufferedOutputStream(String ruta, int n) {
        // TODO
    }

    // ---------- Binario: lectura ----------

    private static void leerFileInputStream(String ruta) {
        // TODO
    }

    private static void leerBufferedInputStream(String ruta) {
        // TODO
    }
}
```

> **Nota:** El `try-with-resources` garantiza que el buffer se vacíe (`flush`) y se cierre al final. Esto es importante porque parte del tiempo puede corresponder al volcado final del buffer al disco.

### Paso 5. Compilar y ejecutar

Desde la terminal, en la carpeta donde está el archivo:

```bash
javac EscrituraBuffered.java
java EscrituraBuffered
```

### Paso 6. Registrar los resultados

Ejecuta el programa al menos **3 veces** para cada valor de `n` y anota los tiempos en una tabla como la siguiente:

| Flujo utilizado         | Tamaño (elementos) | Ejec. 1 (ms) | Ejec. 2 (ms) | Ejec. 3 (ms) | Promedio (ms) |
|-------------------------|--------------------|--------------|--------------|--------------|---------------|
| FileWriter              | 100,000            |              |              |              |               |
| BufferedWriter          | 100,000            |              |              |              |               |
| FileReader              | 100,000            |              |              |              |               |
| BufferedReader          | 100,000            |              |              |              |               |
| FileOutputStream        | 1,000,000          |              |              |              |               |
| BufferedOutputStream    | 1,000,000          |              |              |              |               |
| FileInputStream         | 1,000,000          |              |              |              |               |
| BufferedInputStream     | 1,000,000          |              |              |              |               |

> Repite la tabla para cada valor de `n` (100,000 / 1,000,000 / 10,000,000).

### Paso 7. Comparar escritura/lectura con y sin buffer

Para observar la diferencia, ejecuta cada bloque del código base por separado y compara los tiempos:

- **Texto:** `FileWriter` vs `BufferedWriter` / `FileReader` vs `BufferedReader`.
- **Binario:** `FileOutputStream` vs `BufferedOutputStream` / `FileInputStream` vs `BufferedInputStream`.

---

## Análisis y preguntas para el reporte

1. ¿Qué diferencia observaste entre usar flujos con buffer (`BufferedWriter`, `BufferedReader`, `BufferedOutputStream`, `BufferedInputStream`) y sus equivalentes directos (`FileWriter`, `FileReader`, `FileOutputStream`, `FileInputStream`)? ¿Por qué ocurre?
2. ¿Cómo varía el tiempo de ejecución al aumentar `n`? ¿La relación es lineal? ¿Se comporta igual en lectura que en escritura?
3. ¿Existe diferencia de rendimiento entre trabajar con **caracteres** (texto) y con **bytes** (binario)? ¿A qué se debe?
4. ¿Qué tamaño tiene el buffer por defecto en `BufferedOutputStream`, `BufferedInputStream`, `BufferedWriter` y `BufferedReader`? ¿Puedes cambiarlo? (Pista: constructores con tamaño).
5. ¿Por qué `BufferedReader` ofrece `readLine()` y `BufferedInputStream` no? ¿Qué implicación tiene esto al leer texto?

---

## Entregables

- Código fuente `EscrituraBuffered.java` con los ocho flujos.
- Tabla de resultados con tiempos medidos para cada flujo.
- Respuestas a las preguntas de análisis.
- Conclusión personal sobre el uso de buffers en Java, tanto para texto como para binario.

---

## Recomendaciones finales

- Cierra siempre los flujos con `try-with-resources`.
- Para benchmarks más rigurosos considera **JMH**, ya que la JVM aplica optimizaciones (JIT, escape analysis) que pueden distorsionar mediciones simples.
- Ejecuta varias iteraciones y descarta la primera (calentamiento de la JVM).
- Usa valores de `n` variados para observar tendencias.
- Si comparas texto vs binario, recuerda que `FileWriter`/`FileReader` codifican caracteres según el *charset* por defecto, mientras que los flujos de bytes no.