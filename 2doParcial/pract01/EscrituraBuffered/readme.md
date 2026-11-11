- Resultados

| Flujo utilizado | Tamaño (elementos) | Ejec. 1 (ms) | Ejec. 2 (ms) | Ejec. 3 (ms) | Promedio (ms) |
-----------------------------------------------------------------------------------------------------
| FileWriter | 100,000 | 77.55 | 100.27 | 70.22 | 82.68 |
| BufferedWriter | 100,000 | 19.94 | 20.41 | 23.39 | 21.25 |
| FileReader | 100,000 | 73.03 | 69.38 | 76.94 | 73.12 |
| BufferedReader | 100,000 | 22.71 | 22.60 | 20.14 | 21.82 |
| FileOutputStream | 100,000 | 700.44 | 714.34 | 683.99 | 699.59 |
| BufferedOutputStream | 100,000 | 6.29 | 7.58 | 10.49 | 8.12 |
| FileInputStream | 100,000 | 291.44 | 253.96 | 266.09 | 270.50 |
| BufferedInputStream | 100,000 | 8.28 | 6.08 | 6.21 | 6.86 |


| Flujo utilizado | Tamaño (elementos) | Ejec. 1 (ms) | Ejec. 2 (ms) | Ejec. 3 (ms) | Promedio (ms) |
-----------------------------------------------------------------------------------------------------
| FileWriter | 1,000,000 | 342.99 | 315.81 | 293.71 | 317.50 |
| BufferedWriter | 1,000,000 | 86.12 | 83.65 | 137.82 | 102.53 |
| FileReader | 1,000,000 | 498.10 | 492.68 | 480.66 | 490.48 |
| BufferedReader | 1,000,000 | 74.37 | 74.84 | 73.26 | 74.16 |
| FileOutputStream | 1,000,000 | 6,939.16 | 7,096.02 | 7,045.81 | 7,027.00 |
| BufferedOutputStream | 1,000,000 | 18.96 | 21.55 | 21.45 | 20.65 |
| FileInputStream | 1,000,000 | 2,829.91 | 2,554.98 | 2,612.49 | 2,665.79 |
| BufferedInputStream | 1,000,000 | 18.89 | 16.22 | 16.00 | 17.04 |


| Flujo utilizado | Tamaño (elementos) | Ejec. 1 (ms) | Ejec. 2 (ms) | Ejec. 3 (ms) | Promedio (ms) |
-----------------------------------------------------------------------------------------------------
| FileWriter | 10,000,000 | 2,650.81 | 2,704.25 | 2,632.00 | 2,662.36 |
| BufferedWriter | 10,000,000 | 669.61 | 654.33 | 641.61 | 655.18 |
| FileReader | 10,000,000 | 4,448.25 | 4,594.42 | 4,281.34 | 4,441.33 |
| BufferedReader | 10,000,000 | 494.21 | 506.03 | 528.79 | 509.68 |
| FileOutputStream | 10,000,000 | 81,695.96 | 70,782.36 | 70,406.63 | 74,294.98 |
| BufferedOutputStream | 10,000,000 | 137.94 | 119.44 | 125.02 | 127.47 |
| FileInputStream | 10,000,000 | 25,598.18 | 25,075.95 | 25,602.88 | 25,425.67 |
| BufferedInputStream | 10,000,000 | 100.02 | 99.10 | 102.12 | 100.41 |

 - Comparación con y sin buffer

| Comparación | n = 100,000 | n = 1,000,000 | n = 10,000,000 |
--------------------------------------------------------------
| FileWriter vs BufferedWriter | 3.9x | 3.1x | 4.1x |
| FileReader vs BufferedReader | 3.4x | 6.6x | 8.7x |
| FileOutputStream vs BufferedOutputStream | 86x | 340x | 583x |
| FileInputStream vs BufferedInputStream | 39x | 157x | 253x |


- Preguntas

-¿Qué diferencia hubo entre usar buffer y no usarlo? ¿Por qué?

Con buffer siempre fue más rápido, y en binario la diferencia fue enorme. Sin buffer el programa va al disco por cada byte, y con buffer junta los datos y los manda de un jalón.

-¿Cómo cambia el tiempo al aumentar n? ¿Es lineal? ¿Lectura y escritura se comportan igual?

Casi lineal: si n crece 10 veces, el tiempo también crece unas 10 veces. Con n chico no es tan exacto. Lectura y escritura no se comportaron igual: en binario escribir fue más lento que leer, y en texto fue al revés.

-¿Hay diferencia entre trabajar con texto y con bytes? ¿Por qué?

Sí. Con buffer, los bytes fueron más rápidos porque el texto se tiene que convertir y el archivo de texto pesa más. Sin buffer fue al revés, porque FileWriter y FileReader ya traen un pequeño buffer por dentro.

-¿De qué tamaño es el buffer por defecto? ¿Se puede cambiar?

Los cuatro usan 8192 (8 KB). Sí se puede cambiar, pasándole el tamaño al constructor, por ejemplo new BufferedReader(new FileReader(ruta), 16384).

-¿Por qué BufferedReader tiene readLine() y BufferedInputStream no?

Porque BufferedReader trabaja con texto y sabe qué es una línea, mientras que BufferedInputStream solo ve bytes. Para leer texto por líneas lo mejor es usar BufferedReader.


- Conclusión

Usar buffer sí hace una gran diferencia, lo a mi 