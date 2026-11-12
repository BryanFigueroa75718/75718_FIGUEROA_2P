/*
 * Programa: Simulación de Restaurante con Semáforos
 * Autor: Christopher Martinez Huicab
 * Fecha: 2026/09/08
 * Descripción: Implementa la sincronización entre un cocinero y múltiples meseros
 *              usando semáforos. El cocinero prepara platillos y los meseros los sirven.
 *              Se simula que el mesero es más lento que el cocinero.
 */

#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <time.h>
#include <unistd.h>

#define NR_LOOP 10          // Número de iteraciones para cocinar/servir
#define NUM_WAITERS 3       // Cantidad de meseros que atienden simultáneamente

static void * cocinar(void* arg);
static void * servir(void* arg);

static int counter = 0;     // Contador de platillos preparados (no usado actualmente)

sem_t sem_plato_listo;      // Semáforo que indica cuándo hay un plato listo para servir

int main(void)
{
    pthread_t cocinero;                    // Hilo del cocinero
    pthread_t meseros[NUM_WAITERS];        // Arreglo de hilos para los meseros

    // Inicializa el semáforo con valor 0 (no hay platillos listos inicialmente)
    sem_init(&sem_plato_listo, 0, 0);

    // Crea el hilo del cocinero
    pthread_create(&cocinero, NULL, cocinar, NULL);

    // Crea múltiples hilos de meseros
    for(int i = 0; i < NUM_WAITERS; i++) {
        pthread_create(&meseros[i], NULL, servir, NULL);
    }

    // Espera a que el cocinero termine su trabajo
    pthread_join(cocinero, NULL);

    // Espera a que todos los meseros terminen
    for(int i = 0; i < NUM_WAITERS; i++) {
        pthread_join(meseros[i], NULL);
    }

    printf("Contador %d \n", counter);

    // Destruye el semáforo para liberar recursos
    sem_destroy(&sem_plato_listo);

    return 0;
}

/*
 * Función: cocinar
 * Parámetros: arg - argumento no utilizado (NULL)
 * Retorna: void* - puntero nulo (no se usa)
 * Descripción: Simula la preparación de platillos. Cada iteración prepara un platillo,
 *              lo notifica mediante el semáforo y descansa 1 segundo.
 *              El cocinero es más rápido que los meseros.
 */
static void * cocinar(void* arg) {
    for (int i = 0; i < NR_LOOP; i++) {
        // Simula el tiempo de preparación del platillo
        printf("COCINERO: Comida preparada (platillo #%d)\n", i+1);
        
        // Señala que hay un platillo listo incrementando el semáforo
        sem_post(&sem_plato_listo);
        
        // El cocinero descansa 1 segundo (más rápido que los meseros)
        sleep(1);
    }
    return NULL;
}

/*
 * Función: servir
 * Parámetros: arg - argumento no utilizado (NULL)
 * Retorna: void* - puntero nulo (no se usa)
 * Descripción: Simula el servicio de platillos. Cada mesero espera a que haya
 *              un platillo disponible (mediante sem_wait) y luego lo sirve.
 *              Se simula que el servicio es más lento que la preparación.
 */
static void * servir(void* arg) {
    // Cada mesero sirve exactamente NR_LOOP platillos
    for (int i = 0; i < NR_LOOP; i++) {
        // Espera hasta que haya un platillo listo (decrementa el semáforo)
        sem_wait(&sem_plato_listo);
        
        // Simula el tiempo que tarda en servir el platillo
        // El mesero es más lento que el cocinero (duerme 2 segundos vs 1 segundo)
        printf("MESERO %lu: Comida servida (platillo #%d)\n", pthread_self(), i+1);
        
        // El mesero tarda 2 segundos en servir (más lento que el cocinero)
        sleep(2);
    }
    return NULL;
}