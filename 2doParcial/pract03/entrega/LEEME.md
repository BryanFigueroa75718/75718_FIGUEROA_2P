# Práctica: varios mensajes en una conexión

Archivos para entregar:
- `src/ConcurrentClient.java`: cliente que recibe mensajes del teclado.
- `src/ConcurrentServer.java`: servidor con un hilo por cliente.
- `entrega/explicacion.txt`: explicación breve, en lenguaje sencillo. Léela y adáptala a tu manera de expresarte.
- `entrega/cliente-prueba.txt` y `entrega/servidor-prueba.txt`: registros reales de la prueba automática.
- `entrega/verificacion.txt`: resultados de las comprobaciones.

## Ejecutar en IntelliJ y tomar la captura

1. Ejecuta el `main` de `ConcurrentServer`.
2. Ejecuta el `main` de `ConcurrentClient` sin detener el servidor. No ejecutes `Main.java`: es el ejemplo inicial del proyecto.
3. En la consola del cliente escribe estos mensajes, presionando Enter después de cada uno:

   Hola servidor
   Este es mi segundo mensaje
   Seguimos en la misma conexion

4. Coloca las consolas del cliente y del servidor visibles y toma una captura con la herramienta Recortes de Windows. Guarda la imagen como `entrega/conversacion.png`. Deben verse las tres respuestas y una sola conexión inicial.
5. Escribe `salir` en el cliente. Recibirá «Hasta luego» y terminará. En el servidor aparecerá el cierre de la sesión.
6. Cuando termines la práctica y ya no haya clientes conectados, detén el servidor con el botón Stop del IDE. `salir` cierra la sesión de un cliente, no apaga todo el servidor.

La captura queda pendiente: las herramientas de captura del entorno fallaron al iniciar. Los registros adjuntos son texto real de la ejecución y no sustituyen la captura solicitada.

## Ejecutar desde dos terminales

Desde la carpeta del proyecto, compila:

```powershell
javac -encoding UTF-8 -d out/prueba src/ConcurrentClient.java src/ConcurrentServer.java
```

Primera terminal:

```powershell
java -cp out/prueba ConcurrentServer
```

Segunda terminal:

```powershell
java -cp out/prueba ConcurrentClient
```

Por defecto se usa `localhost` para probar ambos programas en esta computadora. Para conectarte desde otra computadora, cambia la variable host de ConcurrentClient.java por la IP de la computadora donde ejecutaste el servidor, por ejemplo 10.5.84.172, y vuelve a compilar. Luego ejecuta:

```powershell
java -cp out/prueba ConcurrentClient
```

El servidor debe estar activo y ser accesible en el puerto 8080.

## Qué se comprobó

Se compilaron los dos archivos y se enviaron tres mensajes, seguidos de `salir`, utilizando una sola conexión. También se verificó el fin de entrada y que un cliente silencioso no impida responder a otro. Los procesos utilizados para probar se cerraron al finalizar.

Si aparece Address already in use: bind, ya hay un proceso usando el puerto 8080. Detén la ejecución anterior de ConcurrentServer antes de iniciar otra. Sin un servidor escuchando, el cliente muestra un error y termina; no genera respuestas por su cuenta.
