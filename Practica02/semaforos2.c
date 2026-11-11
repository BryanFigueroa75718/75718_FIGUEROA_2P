//Practica 02 - Semáforos
//Autor: Diego Valadez Almeyda - 08/09/2026

#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <time.h>
#include <unistd.h>

#define NR_LOOP 10 // número total de iteraciones por cada hilo
// cocinar: recibe un puntero genérico (sin uso), devuelve un puntero genérico NULL
static void * cocinar(void* arg);
// servir: recibe un puntero genérico (sin uso), devuelve un puntero genérico NULL
static void * servir(void* arg);

static int counter = 0; // recurso compartido para contabilizar operaciones

sem_t sem1; // semáforo para sincronizar la producción y consumo de platos

int main(void)
{
  pthread_t cocinero, mesero1, mesero2; // identificadores de los hilos del sistema

  // inicializa el semáforo sem1 compartido entre hilos (0) con valor inicial 0 para bloquear meseros hasta cocinar
  sem_init(&sem1, 0, 0);

  
  
  // crea el hilo productor que ejecutará la rutina cocinar
  pthread_create (&cocinero, NULL, *cocinar, NULL);
  // crea el primer hilo consumidor para la rutina servir
  pthread_create (&mesero1, NULL, *servir, NULL);
  // crea el segundo hilo consumidor para la rutina servir
  pthread_create (&mesero2, NULL, *servir, NULL);

  // espera la finalización del hilo del cocinero
  pthread_join(cocinero, NULL);
  // espera la finalización del primer hilo mesero
  pthread_join(mesero1, NULL);
  // espera la finalización del segundo hilo mesero
  pthread_join(mesero2, NULL);

  printf("Contador %d \n", counter);

  return 0;
}

// cocinar: recibe un puntero genérico (sin uso), devuelve un puntero genérico NULL
static void * cocinar(void* arg) {
  for (int i = 0; i < NR_LOOP; i++) // itera 10 veces para producir comida
  {
    printf("COCINERO: Comida preparada \n");
    sem_post(&sem1); // incrementa el semáforo para notificar que un plato está listo
    sleep(1); // pausa de 1 segundo para simular el tiempo de cocción
  }
  return NULL;
}

// servir: recibe un puntero genérico (sin uso), devuelve un puntero genérico NULL
static void * servir(void* arg) {
  for (int i = 0; i < NR_LOOP/2; i++) // itera 10 veces intentando servir comida
  {
    sem_wait(&sem1); // decrementa o espera hasta que haya comida disponible
    printf("MESERO: Comida servida \n");
    counter++;
    sleep(3); // pausa de 3 segundos para simular el tiempo de servicio
  }
  return NULL;
}