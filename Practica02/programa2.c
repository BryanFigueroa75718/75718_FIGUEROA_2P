/*
 * Programa: Cocinero y Mesero con Semaforos
 * Autor: Alan Campos
 * Fecha: 08/09/2026
 * Descripcion: Simula la preparacion y servicio de comida
 *              utilizando hilos y un semaforo para sincronizarlos.
 */

#include <pthread.h>   // Permite trabajar con hilos
#include <semaphore.h> // Permite utilizar semaforos
#include <stdio.h>     // Funciones de entrada y salida
#include <time.h>      // Funciones relacionadas con el tiempo
#include <unistd.h>    // Permite utilizar sleep()

#define NR_LOOP 10 // Cantidad de veces que se prepara y sirve comida

// Funciones que reciben un argumento y devuelven un puntero
static void * cocinar(void* arg);
static void * servir(void* arg);

static int counter = 0; // Contador del programa

sem_t sem1; // Semaforo utilizado para sincronizar al cocinero y al mesero

int main(void)
{
  pthread_t cocinero, mesero; // Identificadores de los dos hilos

  sem_init(&sem1, 0, 0); // Inicializa el semaforo con valor 0

  // Crea el hilo que ejecutara la funcion cocinar
  pthread_create (&cocinero, NULL, *cocinar, NULL);

  // Crea el hilo que ejecutara la funcion servir
  pthread_create (&mesero, NULL, *servir, NULL);

  // Espera a que termine el hilo del cocinero
  pthread_join(cocinero, NULL);

  // Espera a que termine el hilo del mesero
  pthread_join(mesero, NULL);

  printf("Contador %d \n", counter); // Muestra el valor final del contador

  return 0; // Finaliza el programa
}

// Funcion cocinar: recibe un puntero como argumento y no devuelve un valor util
static void * cocinar(void* arg) {

  // Recorre el ciclo 10 veces para preparar la comida
  for (int i = 0; i < NR_LOOP; i++)
  {
    // sem_wait(&sem1);
    // counter++;
    // sem_post(&sem1);

    printf("COCINERO: Comida preparada \n"); // Muestra que la comida esta lista

    sem_post(&sem1); // Libera el semaforo para avisar al mesero

    sleep(1); // Espera 1 segundo antes de preparar otra comida
  }
  
}

// Funcion servir: recibe un puntero como argumento y no devuelve un valor util
static void * servir(void* arg) {

  // Recorre el ciclo 10 veces para servir la comida
  for (int i = 0; i < NR_LOOP; i++)
  {
    sem_wait(&sem1); // Espera hasta que el cocinero prepare una comida

    printf("MESERO: Comida servida \n"); // Muestra que la comida fue servida
  }
  
}