// Problema de "Semaforos" entre cocinero y mesero
// Alumno: Pedro Alejandro Chavez Rivero
// Docente: Sergio Andres Noh Puch
// Fecha: 08 de septiembre del 2023
// El presente programa simula la coordinación entre un cocinero que prepara comida y un mesero que la sirve haciendo uso de semáforos.

#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <time.h>
#include <unistd.h>

#define NR_LOOP 10 // Es el numero de veces que repetiremos el proceso o "Loop"

static void * cocinar(void* arg);
static void * servir(void* arg);

static int counter = 0; // Es el contador global de los platillos
sem_t sem1;

int main(void) {
    pthread_t cocinero, mesero;

    sem_init(&sem1, 0, 0); // Aunque podemos cambiar el valor, para la practica se inicia el semáforo en 0

    pthread_create(&cocinero, NULL, cocinar, NULL);
    pthread_create(&mesero, NULL, servir, NULL);

    pthread_join(cocinero, NULL); // Sirve como punto de control hasta que el cocinero termine
    pthread_join(mesero, NULL); // Lo mismo que el anterior solo que para el mesero

    printf("Contador total de platillos: %d \n", counter);

    return 0;
}

static void * cocinar(void* arg) {
    for (int i = 0; i < NR_LOOP; i++) { // Aqui recorremos el numero de veces que se prepararan los platillos
        counter++;
        printf("COCINERO: Comida preparada \n");
        
        sem_post(&sem1); // Aqui se incrementa el semáforo para indicar que ya se puede servir el platillo
        
        sleep(1); // Pausamos la ejecucion aunque solo es con fines de simular un tiempo de preparacion
    }
    return NULL;
}

static void * servir(void* arg) {
    for (int i = 0; i < NR_LOOP; i++) { // Nuevamente recorremos el numero de veces que se serviran los platillos
        sem_wait(&sem1); //Bloqueamos el semáforo hasta que el valor sea mayor a 0
        printf("MESERO: Comida servida \n");
    }
    return NULL;
}