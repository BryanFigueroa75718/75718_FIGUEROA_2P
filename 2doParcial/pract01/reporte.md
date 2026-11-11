Nombre: Leidy Miriam Morayta Pérez  
Alumna de 5to de ITS  
Materia: Concurrencia y paralelismo

# Práctica: flujos con y sin buffer

## Introducción

En esta práctica se compararon los tiempos de lectura y escritura de archivos de texto y binarios en Java. El objetivo fue observar cómo ayuda un buffer cuando se trabaja con muchos datos.

## ¿Qué se hizo?

Se utilizaron ocho clases de Java para escribir y leer letras de la A a la Z, un elemento a la vez. Se probaron 100,000, 1,000,000 y 10,000,000 de elementos, con tres mediciones para cada tamaño. 

## Resultados

Los tiempos  están expresados en milisegundos.

### 100,000 elementos

| Flujo | Elementos | Ejec. 1 (ms) | Ejec. 2 (ms) | Ejec. 3 (ms) | Promedio (ms) |
|---|---:|---:|---:|---:|---:|
| FileWriter | 100000 | 14.193 | 44.224 | 13.042 | 23.820 |
| BufferedWriter | 100000 | 8.020 | 4.154 | 5.548 | 5.907 |
| FileReader | 100000 | 44.408 | 7.422 | 7.997 | 19.943 |
| BufferedReader | 100000 | 8.080 | 4.387 | 6.745 | 6.404 |
| FileOutputStream | 100000 | 1339.145 | 1233.319 | 1238.714 | 1270.393 |
| BufferedOutputStream | 100000 | 7.252 | 4.372 | 3.440 | 5.021 |
| FileInputStream | 100000 | 410.049 | 379.805 | 382.974 | 390.943 |
| BufferedInputStream | 100000 | 3.827 | 10.795 | 3.465 | 6.029 |

### 1,000,000 elementos

| Flujo | Elementos | Ejec. 1 (ms) | Ejec. 2 (ms) | Ejec. 3 (ms) | Promedio (ms) |
|---|---:|---:|---:|---:|---:|
| FileWriter | 1000000 | 82.621 | 141.990 | 130.482 | 118.364 |
| BufferedWriter | 1000000 | 27.989 | 28.456 | 32.163 | 29.536 |
| FileReader | 1000000 | 49.139 | 50.637 | 54.572 | 51.449 |
| BufferedReader | 1000000 | 33.092 | 26.234 | 26.789 | 28.705 |
| FileOutputStream | 1000000 | 12591.275 | 27167.350 | 13411.217 | 17723.281 |
| BufferedOutputStream | 1000000 | 34.403 | 126.369 | 27.248 | 62.673 |
| FileInputStream | 1000000 | 3438.226 | 9440.241 | 3564.216 | 5480.894 |
| BufferedInputStream | 1000000 | 36.140 | 27.828 | 28.119 | 30.695 |

### 10,000,000 elementos

| Flujo | Elementos | Ejec. 1 (ms) | Ejec. 2 (ms) | Ejec. 3 (ms) | Promedio (ms) |
|---|---:|---:|---:|---:|---:|
| FileWriter | 10000000 | 911.614 | 871.198 | 863.600 | 882.138 |
| BufferedWriter | 10000000 | 290.330 | 346.569 | 285.598 | 307.499 |
| FileReader | 10000000 | 575.151 | 607.722 | 577.568 | 586.814 |
| BufferedReader | 10000000 | 303.841 | 349.260 | 290.702 | 314.601 |
| FileOutputStream | 10000000 | 172936.478 | 126854.058 | 126014.314 | 141934.950 |
| BufferedOutputStream | 10000000 | 288.938 | 261.476 | 260.621 | 270.345 |
| FileInputStream | 10000000 | 58285.885 | 35339.059 | 34289.869 | 42638.271 |
| BufferedInputStream | 10000000 | 359.978 | 352.540 | 349.225 | 353.914 |

## Preguntas

### 1. ¿Qué diferencia observaste entre usar flujos con buffer y sin buffer? ¿Por qué ocurre?

Los flujos con buffer tuvieron menores tiempos promedio. La diferencia fue más grande al trabajar con bytes: para 10,000,000 de elementos, la escritura sin buffer tardó en promedio 141,934.950 ms y con buffer 270.345 ms. Esto ocurre porque el buffer reúne datos en memoria y los envía o recibe en bloques, reduciendo las operaciones pequeñas sobre el archivo. (Como al cargar los videos youtube)

### 2. ¿Cómo varía el tiempo al aumentar n? ¿La relación es lineal? ¿Se comporta igual en lectura y escritura?

En general, el tiempo aumentó al incrementar la cantidad de elementos. Sin embargo, la relación no fue exacta porque también influyen la memoria caché y la actividad del equipo. La lectura y la escritura no tardaron lo mismo; la escritura binaria sin buffer fue más lenta que su lectura.

### 3. ¿Existe diferencia entre trabajar con caracteres y con bytes? ¿A qué se debe?

Sí hay diferencia porque el texto necesita convertir caracteres a bytes y viceversa, mientras que en los flujos binarios trabajan directamente con los bytes. Aun así, el binario no siempre era más rápido pues al procesar un byte por llamada sin buffer resultó muy lento. Las clases de texto ya tienen cierto almacenamiento interno, por lo que también importa cómo trabaja cada clase.

### 4. ¿Qué tamaño tiene el buffer por defecto? ¿Puedes cambiarlo?

En Java 23.0.2, para el hilo principal, BufferedOutputStream y BufferedInputStream usan 8,192 bytes. BufferedWriter y BufferedReader usan 8,192 caracteres. Sí se puede cambiar: las cuatro clases tienen un constructor que permite indicar un tamaño pero puede depender que la version de java que use. 

### 5. ¿Por qué BufferedReader ofrece readLine() y BufferedInputStream no? ¿Qué implica al leer texto?

BufferedReader trabaja con caracteres y reconoce los saltos de línea, por eso permite leer una línea completa con readLine(). BufferedInputStream solo maneja bytes y no interpreta su significado como texto. Para leer texto por líneas conviene usar BufferedReader con un condicional.

## Conclusión

Con esta práctica comprendí que usar un buffer puede reducir mucho el tiempo de lectura y escritura, principalmente cuando quiero procesa datos uno por uno. También observé que los tiempos cambian entre ejecuciones, por lo que es útil repetir las pruebas y comparar los promedios para calentar el cpu.
