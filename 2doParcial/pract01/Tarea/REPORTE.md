# **REPORTE**



##### **Introducción**



En esta práctica trabajé con diferentes clases de Java para leer y escribir archivos de texto y binarios, comparando los tiempos de ejecución de los flujos con buffer y sin buffer. Para realizar las mediciones utilicé System.nanoTime() y probé con distintas cantidades de datos para observar cómo cambiaban los resultados



##### **Desarrollo**



Primero creé el archivo EscrituraBuffered.java e implementé los ocho métodos solicitados: FileWriter, BufferedWriter, FileReader, BufferedReader, FileOutputStream, BufferedOutputStream, FileInputStream y BufferedInputStream



Después realicé las pruebas con 100,000, 1,000,000 y 10,000,000 de elementos. Cada prueba se ejecutó tres veces y calculé el promedio de los tiempos obtenidos para comparar los resultados



**100,000 elementos**



Flujo utilizado     Ejec. 1 (ms)      Ejec. 2 (ms)        Ejec. 3 (ms)       Promedio (ms)



FileWriter          44.194            37.965              47.368             43.176



BufferedWriter      13.223            23.288              19.640             18.717



FileReader          49.855            60.965              61.763             57.528



BufferedReader      12.893            12.538              9.948              11.793



FileOutputStream    277.610           323.971             247.645            283.075



BufferedOutputStream 5.885             6.033              7.687              6.535



FileInputStream      159.060           159.874            154.506            157.813



BufferedInputStream  5.831             5.160              5.892              5.628



**1,000,000 elementos**



Flujo utilizado     Ejec. 1 (ms)      Ejec. 2 (ms)        Ejec. 3 (ms)       Promedio (ms)



FileWriter          200.223           164.431             165.333            176.662



BufferedWriter      80.971            86.061              87.521             84.851



FileReader          490.318           440.522             477.109            469.316



BufferedReader      52.633            61.880              43.747             52.753



FileOutputStream    2453.640          2375.001            2358.348           2395.663



BufferedOutputStream 26.992           27.176              26.734             26.967



FileInputStream     1457.323          1455.331            1447.377           1453.344



BufferedInputStream 25.199            27.130              25.063             25.797



**10,000,000 elementos**



Flujo utilizado     Ejec. 1 (ms)      Ejec. 2 (ms)        Ejec. 3 (ms)       Promedio (ms)



FileWriter          1421.339          1352.426            1292.284           1355.350



BufferedWriter      701.569           689.388             638.797            676.585



FileReader          4198.207          4068.792            4023.505           4096.835



BufferedReader      425.064           410.390             418.158            417.871



FileOutputStream    22746.958         22654.265           22343.785          22581.669



BufferedOutputStream 241.849          249.159             249.913            246.974



FileInputStream    14602.528         14553.671            14307.540          14487.913



BufferedInputStream 233.768          236.441              242.119            237.443





Al comparar los resultados observé que los flujos con buffer fueron más rápidos que sus equivalentes sin buffer. Esto ocurre porque almacenan temporalmente los datos y permiten trabajar con ellos en bloques, reduciendo las operaciones directas con el archivo



También observé que el tiempo de ejecución aumentó al utilizar cantidades mayores de datos, aunque no lo hizo en la misma proporción para todos los flujos. La lectura y escritura tuvieron resultados diferentes dependiendo de la clase utilizada



En cuanto a los archivos de texto y binarios, los primeros trabajan con caracteres, mientras que los segundos trabajan directamente con bytes. Esto influye en la forma de procesar la información y en el tiempo necesario para completar las operaciones



Los buffers tienen normalmente un tamaño predeterminado de 8192 unidades: bytes para BufferedInputStream y BufferedOutputStream, y caracteres para BufferedReader y BufferedWriter. Este tamaño se puede modificar mediante los constructores correspondientes



Por último, BufferedReader cuenta con el método readLine(), que permite leer una línea completa de texto. BufferedInputStream no tiene ese método porque trabaja con bytes y no interpreta directamente los saltos de línea como caracteres



##### **Conclusión**



Con esta práctica comprobé que el uso de buffers puede mejorar considerablemente el rendimiento al leer y escribir archivos en Java. La diferencia fue especialmente notable al trabajar con 10,000,000 de elementos. Por ejemplo, FileOutputStream tardó aproximadamente 22.58 segundos, mientras que BufferedOutputStream tardó cerca de 0.25 segundos



Aprendí que elegir correctamente las clases para trabajar con archivos es importante cuando se necesita procesar una gran cantidad de información. También comprendí la utilidad de medir los tiempos de ejecución y calcular promedios para comparar el rendimiento de diferentes métodos

