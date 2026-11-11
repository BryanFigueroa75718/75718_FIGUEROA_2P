# Explicación del funcionamiento

# Aaron David Chi Uc 5° A ITS 

Este programa tiene dos partes: un servidor y un cliente que se comunican por red usando sockets.

El servidor se queda esperando conexiones en el puerto 8080. Cada vez que un cliente se conecta, el servidor crea un hilo nuevo para atenderlo, y así puede atender a varios clientes al mismo tiempo sin que uno bloquee a los demás. Dentro de ese hilo, el servidor lee los mensajes que llegan uno por uno y le responde al cliente lo que recibió.

El cliente se conecta al servidor y le pide al usuario que escriba mensajes por teclado. Cada mensaje se envía al servidor, y el cliente espera la respuesta para mostrarla en pantalla. Esto se repite en un ciclo, por lo que la conexión sigue abierta y se pueden mandar todos los mensajes que se quiera sin volver a conectarse.

Antes, el cliente enviaba un solo mensaje y terminaba, y el servidor ni siquiera tenía código para atenderlo. Ahora ambos tienen un ciclo que mantiene viva la comunicación.

Para terminar, el usuario escribe "salir". El cliente lo envía, el servidor responde con una despedida y ambos rompen su ciclo. Al hacerlo, los sockets se cierran automáticamente y el hilo del servidor termina, sin dejar nada pendiente.

Si el cliente se cierra de golpe, el servidor lo detecta porque la lectura devuelve `null`, y también cierra todo de forma ordenada.
