# Práctica de flujos con y sin buffer - Laura Lucia Jaramillo Yah - 17/10/2026

## Objetivo

Comparar el tiempo que tarda Java en escribir y leer archivos usando flujos con y sin buffer. Para medir el tiempo se usó `System.nanoTime()`.

## Marco teórico

`FileWriter` y `FileReader` sirven para trabajar con archivos de texto. `BufferedWriter` y `BufferedReader` hacen algo parecido, pero usan un buffer para trabajar con los datos por partes.

`FileOutputStream` y `FileInputStream` sirven para escribir y leer bytes. `BufferedOutputStream` y `BufferedInputStream` también trabajan con bytes, pero usan un buffer para reducir las operaciones pequeñas.

`System.nanoTime()` permite medir el tiempo de una operación. En el programa se restó el tiempo inicial al final y se convirtió el resultado a milisegundos.

## Desarrollo

Se hizo un programa en Java con los ocho flujos que pide la práctica. Se probaron tres cantidades de elementos: 100,000, 1,000,000 y 10,000,000. Cada prueba se ejecutó tres veces y se calculó el promedio.

## Resultados

### 100,000 elementos

| Flujo | Prueba 1 (ms) | Prueba 2 (ms) | Prueba 3 (ms) | Promedio (ms) |
|---|---:|---:|---:|---:|
| FileWriter | 153.312 | 135.010 | 131.070 | 139.797 |
| BufferedWriter | 50.340 | 46.904 | 41.379 | 46.208 |
| FileReader | 173.395 | 183.501 | 175.017 | 177.304 |
| BufferedReader | 26.917 | 27.343 | 22.558 | 25.606 |
| FileOutputStream | 954.144 | 781.373 | 773.497 | 836.338 |
| BufferedOutputStream | 14.098 | 9.812 | 8.710 | 10.873 |
| FileInputStream | 551.927 | 451.776 | 459.815 | 487.839 |
| BufferedInputStream | 15.752 | 7.113 | 9.111 | 10.659 |

### 1,000,000 de elementos

| Flujo | Prueba 1 (ms) | Prueba 2 (ms) | Prueba 3 (ms) | Promedio (ms) |
|---|---:|---:|---:|---:|
| FileWriter | 591.380 | 575.401 | 579.817 | 582.199 |
| BufferedWriter | 212.390 | 138.899 | 141.835 | 164.375 |
| FileReader | 694.283 | 697.615 | 729.681 | 707.193 |
| BufferedReader | 85.878 | 80.922 | 86.238 | 84.346 |
| FileOutputStream | 8073.253 | 8275.049 | 7919.650 | 8089.317 |
| BufferedOutputStream | 24.717 | 27.655 | 27.325 | 26.566 |
| FileInputStream | 4465.896 | 4764.381 | 4551.744 | 4594.007 |
| BufferedInputStream | 25.074 | 23.824 | 26.705 | 25.201 |

### 10,000,000 de elementos

| Flujo | Prueba 1 (ms) | Prueba 2 (ms) | Prueba 3 (ms) | Promedio (ms) |
|---|---:|---:|---:|---:|
| FileWriter | 4653.413 | 4130.456 | 4129.975 | 4304.615 |
| BufferedWriter | 1154.086 | 1154.848 | 1295.686 | 1201.540 |
| FileReader | 6274.124 | 6599.007 | 6888.281 | 6587.137 |
| BufferedReader | 547.753 | 559.101 | 608.178 | 571.677 |
| FileOutputStream | 78699.187 | 79514.886 | 77750.054 | 78654.709 |
| BufferedOutputStream | 184.394 | 181.378 | 177.501 | 181.091 |
| FileInputStream | 45239.211 | 44660.693 | 44717.779 | 44872.561 |
| BufferedInputStream | 174.208 | 165.921 | 180.635 | 173.588 |

## Respuestas

### 1. ¿Qué diferencia hubo entre usar flujos con buffer y sin buffer?

En mis pruebas, los flujos con buffer tardaron menos. Esto pasa porque guardan datos temporalmente y los escriben o leen por partes. Así se evitan muchas operaciones pequeñas. La diferencia se notó más en los flujos binarios.

### 2. ¿Qué pasa con el tiempo cuando aumenta la cantidad de elementos?

Cuando aumenté la cantidad de elementos, el tiempo también aumentó. No creció exactamente igual en todos los casos, porque depende de la computadora y de cómo se ejecuta el programa. La lectura y la escritura tampoco tardaron lo mismo.

### 3. ¿Hay diferencia entre trabajar con caracteres y con bytes?

Sí, los tiempos fueron diferentes. Los flujos de texto trabajan con caracteres y los binarios con bytes. Además, el programa no genera la misma cantidad de información en ambos casos: el archivo de texto guarda líneas y el binario guarda un byte por elemento. Por eso no se pueden comparar directamente como si procesaran exactamente lo mismo.

### 4. ¿Qué tamaño tiene el buffer por defecto? ¿Se puede cambiar?

En las versiones habituales de Java, el tamaño predeterminado es de 8,192 unidades: caracteres para los flujos de texto y bytes para los binarios. Sí se puede cambiar al crear el flujo, indicando otro tamaño en el constructor. Por ejemplo:

```java
BufferedOutputStream archivo =
    new BufferedOutputStream(new FileOutputStream(ruta), 16384);
```

En este caso, el buffer es de 16,384 bytes.

### 5. ¿Por qué `BufferedReader` tiene `readLine()` y `BufferedInputStream` no?

`BufferedReader` trabaja con texto y por eso puede leer una línea completa con `readLine()`. `BufferedInputStream` trabaja con bytes y no sabe dónde termina una línea de texto. Para leer texto desde bytes hay que convertirlos usando una codificación.

## Conclusión

Con esta práctica vi que usar buffer puede hacer que leer y escribir archivos sea mucho más rápido. En mis resultados, los flujos con buffer tardaron menos, sobre todo al trabajar con bytes.

También noté que el tiempo aumenta al procesar más elementos y que puede cambiar un poco entre una prueba y otra. Por eso hice tres ejecuciones y calculé el promedio. En general, entendí que el buffer ayuda a trabajar con archivos de forma más rápida, aunque depende del tipo de datos y de la operación.
