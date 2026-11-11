# Reporte de práctica 06: Lectura y escritura de archivos con y sin buffer

**Alumno:** Aaron David Chi Uc  
**Materia:** Concurrencia y Paralelismo  
**Práctica:** Comparación de lectura y escritura de archivos en Java

## Objetivo

Comparar el tiempo que tarda Java en leer y escribir archivos de texto y binarios, usando métodos con buffer y sin buffer.

## Introducción

En Java podemos usar diferentes clases para guardar información en archivos y también para leerla. Algunas clases usan un buffer, que es un espacio de memoria donde se pueden juntar los datos para trabajarlos en grupos. En esta práctica comparé ocho métodos para ver cuáles tardaban menos.

## Desarrollo

Primero preparé cuatro métodos para trabajar con texto y cuatro para trabajar con bytes. En cada método utilicé System.nanoTime() para medir el tiempo y try-with-resources para que los archivos se cerraran al terminar.

Después llamé a los ocho métodos desde mi main. Para esta prueba utilicé **n = 1,000,000** y ejecuté el programa desde la terminal:

```bash
javac main.java
java main
```

El programa imprimió los tiempos de cada operación. En la lectura conté los caracteres o bytes, sin imprimir todo el contenido, para no afectar tanto la medición.

## Tabla de resultados obtenidos

Estos son los **valores reales** que aparecieron en mi terminal:

| Operación | Método | Tiempo (ns) | Tiempo (ms) |
|---|---|---:|---:|
| Escritura de texto sin buffer | `FileWriter` | 637731300 | 637.7313 |
| Escritura de texto con buffer | `BufferedWriter` | 139932400 | 139.9324 |
| Lectura de texto sin buffer | `FileReader` | 559387800 | 559.3878 |
| Lectura de texto con buffer | `BufferedReader` | 209060100 | 209.0601 |
| Escritura binaria sin buffer | `FileOutputStream` | 8976280500 | 8976.2805 |
| Escritura binaria con buffer | `BufferedOutputStream` | 27761800 | 27.7618 |
| Lectura binaria sin buffer | `FileInputStream` | 3496828500 | 3496.8285 |
| Lectura binaria con buffer | `BufferedInputStream` | 23475800 | 23.4758 |

En las dos lecturas de texto se contaron **17,888,890 caracteres**. En las dos lecturas binarias se contaron **1,000,000 de bytes**.

## Preguntas de análisis

### 1. ¿Qué diferencia observaste al usar métodos con buffer y sin buffer? ¿Por qué ocurre?

Observé que los cuatro métodos con buffer fueron más rápidos. Por ejemplo, la escritura binaria sin buffer tardó **8976.2805 ms**, pero con buffer solo **27.7618 ms**. Esto ocurre porque el buffer permite juntar varios datos y trabajar con ellos en bloques. Así no se necesita hacer una operación sobre el archivo por cada dato pequeño.

### 2. ¿Cómo varía el tiempo al aumentar `n`? ¿La relación es lineal? ¿Es igual en lectura y escritura?

Cuando aumenta `n`, normalmente hay más datos que escribir y leer, por lo que el tiempo también suele aumentar. En algunos casos el tiempo puede crecer de forma parecida a la cantidad de datos, pero no siempre será exactamente lineal, porque también influyen el sistema operativo, la memoria y el disco.

**En mi prueba solo medí `n = 1,000,000`**, así que no puedo asegurar con mis resultados si el crecimiento fue lineal. Tampoco puedo decir que lectura y escritura crezcan igual: para comprobarlo tendría que repetir las pruebas con otros valores de `n`.

### 3. ¿Hay diferencia entre trabajar con caracteres y con bytes? ¿Por qué?

Sí hay diferencia. Los métodos de texto trabajan con caracteres y convierten la información según una codificación, mientras que los métodos binarios trabajan directamente con bytes. Además, en esta práctica escribí líneas como `Hola Mundo0`, `Hola Mundo1`, etc., en texto, pero en binario escribí un byte por cada vuelta del ciclo. Por eso **no guardé exactamente la misma cantidad ni el mismo tipo de información** en ambos archivos.

En mis resultados, la escritura binaria sin buffer tardó más que la escritura de texto sin buffer. En cambio, la escritura binaria con buffer fue muy rápida. Esto no significa que el formato binario siempre sea más lento o más rápido: depende de cómo se escriben y leen los datos.

### 4. ¿Cuál es el tamaño del buffer por defecto? ¿Se puede cambiar?

En las implementaciones habituales de Java, los tamaños usados por defecto son:

| Clase | Tamaño habitual por defecto |
|---|---|
| `BufferedOutputStream` | 8,192 bytes |
| `BufferedInputStream` | 8,192 bytes |
| `BufferedWriter` | 8,192 caracteres |
| `BufferedReader` | 8,192 caracteres |

Sí puedo cambiar el tamaño al crear el objeto. 

### 5. ¿Por qué BufferedReader tiene readLine() y  BufferedInputStream no? ¿Qué implica al leer texto?

BufferedReader sirve para leer caracteres y tiene el método readLine(), que permite obtener una línea completa de texto sin tener que revisar cada carácter por separado.

BufferedInputStream trabaja con bytes y no interpreta por sí mismo dónde empiezan o terminan las líneas. Si quiero leer texto desde un flujo de bytes, puedo usar un InputStreamReader para convertir los bytes en caracteres y luego un BufferedReader para leer por líneas. También debo utilizar la codificación correcta.

## Conclusión personal

Con esta práctica aprendí que el buffer puede ayudar bastante cuando trabajamos con archivos en Java. Al comparar los ocho métodos pude ver que todos los que usaron buffer tardaron menos en mi prueba. La diferencia fue más grande en los métodos binarios que escribían o leían un byte a la vez.

También entendí que los archivos de texto y los binarios no se manejan exactamente igual. Considero que utilizar buffers es una buena opción cuando necesitamos leer o escribir muchos datos, aunque para comparar mejor los métodos sería útil repetir las pruebas con otras cantidades de elementos.

## Código fuente entregado

El código completo está en el archivo **`EscrituraBuffered.java`**, que contiene estos ocho métodos públicos y estáticos: `escribirFileWriter`, `escribirBufferedWriter`, `leerFileReader`, `leerBufferedReader`, `escribirFileOutputStream`, `escribirBufferedOutputStream`, `leerFileInputStream` y `leerBufferedInputStream`. La clase `main` se mantiene por separado.
