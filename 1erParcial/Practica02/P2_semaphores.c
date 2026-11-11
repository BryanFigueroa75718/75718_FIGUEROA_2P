// Sincronización con Semáforos – Autor: Victor Adrian Romero Minaya – 2026/09/07
// El programa coordina a un cocinero y un mesero asegurando que el mesero solo sirva después de que el cocinero haya sacado la chamba :). Usamos un semáforo para darnos luz verde entre los hilos.

#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <time.h>
#include <unistd.h>

#define NR_LOOP 10 // cantidad de platillos que vamos a preparar y servir en esta prueba

// Declaración de las funciones que van a ejecutar nuestros hilos
static void * cocinar(void* arg);
static void * servir(void* arg);

static int counter = 0; // variable global compartida (aunque en este ejemplo no la estamos alterando)

sem_t sem1; // nuestro semáforo principal. Pone en alto al mesero hasta que hay comida disponible.

// main: no recibe nada, devuelve un entero. Es el hilo principal que arma todo el espacio y pone a trabajar a los demás.
int main(void)
{
  pthread_t cocinero, mesero; // identificadores para nuestros hilos (como los gafetes de los procesos jsja)

  // Inicializamos el semáforo en 0. Básicamente significa que empezamos sin comida, el recurso está vacío desde el inicio.
  sem_init(&sem1, 0, 0);

  // Creamos los hilos y los mandamos al estado "Listo" para que el planificador del SO los empiece a ejecutar.
  pthread_create (&cocinero, NULL, cocinar, NULL);
  pthread_create (&mesero, NULL, servir, NULL);

  // El hilo principal se queda esperando aquí (bloqueado) hasta que el cocinero y el mesero terminen todo su ciclo para poder cerrar bien el programa.
  pthread_join(cocinero, NULL);
  pthread_join(mesero, NULL);

  printf("Contador %d \n", counter);

  // Saqué la hora del sistema para imprimirla al final y así poder diferenciar fácilmente las 3 capturas de pantalla de las pruebas :D
  time_t tiempo_actual = time(NULL); // guarda la fecha y hora exacta del sistema
  printf("Hora: %s", ctime(&tiempo_actual));

  return 0; // cerramos el programa sin errores
}

// cocinar: recibe un puntero vacío, devuelve un puntero vacío. Es la tarea de preparar los platillos.
static void * cocinar(void* arg) {
  for (int i = 0; i < NR_LOOP; i++) // repetimos el ciclo exactamente las veces que configuramos arriba
  {
    printf("COCINERO: Comida preparada \n");
    
    // El cocinero hace un signal (post). Esto incrementa el semáforo y despierta al mesero si es que estaba bloqueado esperando la orden.
    sem_post(&sem1);
    
    // Le damos 1 segundo de pausa para simular el tiempo real que tarda en cocinar y así poder ver bien la intercalación en la consola.
    sleep(1);
  }
  return NULL; // terminamos la ejecución del hilo
}

// servir: recibe un puntero vacío, devuelve un puntero vacío. Es la tarea de llevar la orden a la mesa.
static void * servir(void* arg) {
  for (int i = 0; i < NR_LOOP; i++) // el mesero intenta servir la misma cantidad de platos
  {
    // El mesero hace un wait. Checa el semáforo, si está en 0 (no hay comida), se queda dormido esperando (porque no hay chamba). Si hay comida (mayor a 0), resta 1 y sigue su camino.
    sem_wait(&sem1); 
    
    printf("MESERO: Comida servida \n");
  }
  return NULL; // terminamos la ejecución del hilo c:
}