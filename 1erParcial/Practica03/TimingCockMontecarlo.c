#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <stdint.h>
#include <time.h>

//Cesar Raul Ramirez Cab 69967

#define TOTAL_PUNTOS 1000000L
#define NUM_HILOS 8 

long puntos_dentro_total = 0;
sem_t semaforo;

void* calcular_pi(void* arg) {
    long id_hilo = (long)arg;

   
    long puntos_por_hilo = TOTAL_PUNTOS / NUM_HILOS;
    long inicio = id_hilo * puntos_por_hilo;
    long fin = (id_hilo == NUM_HILOS - 1) ? TOTAL_PUNTOS : (inicio + puntos_por_hilo);
    long total_puntos_hilo = fin - inicio;

    long puntos_dentro_local = 0;

    
    unsigned int seed = (unsigned int)time(NULL) ^ (unsigned int)(uintptr_t)pthread_self() ^ (unsigned int)id_hilo;

    for (long i = 0; i < total_puntos_hilo; i++) {

        double x = ((double)rand_r(&seed) / RAND_MAX) * 2.0 - 1.0;
        double y = ((double)rand_r(&seed) / RAND_MAX) * 2.0 - 1.0;

        if (x * x + y * y <= 1.0) {
            puntos_dentro_local++;
        }
    }

    printf("[Hilo %ld] Puntos dentro locales: %ld / %ld\n", 
           id_hilo, puntos_dentro_local, total_puntos_hilo);

    
    sem_wait(&semaforo);
    puntos_dentro_total += puntos_dentro_local;
    sem_post(&semaforo);

    return NULL;
}

int main(void) {
    pthread_t hilos[NUM_HILOS];
    struct timespec inicio, fin;

    sem_init(&semaforo, 0, 1);

 
    clock_gettime(CLOCK_MONOTONIC, &inicio);

    for (long i = 0; i < NUM_HILOS; i++) {
        if (pthread_create(&hilos[i], NULL, calcular_pi, (void*)i) != 0) {
            perror("Error al crear el hilo");
            return 1;
        }
    }

    
    for (int i = 0; i < NUM_HILOS; i++) {
        pthread_join(hilos[i], NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &fin);

  
    sem_destroy(&semaforo);

    double tiempo = (fin.tv_sec - inicio.tv_sec) + 
                    (fin.tv_nsec - inicio.tv_nsec) / 1e9;

    long puntos_fuera_total = TOTAL_PUNTOS - puntos_dentro_total;
    double pi_estimado = 4.0 * (double)puntos_dentro_total / TOTAL_PUNTOS;

    printf("\n--- RESULTADO FINAL ---\n");
    printf("Puntos totales: %ld\n", TOTAL_PUNTOS);
    printf("Puntos dentro del circulo: %ld\n", puntos_dentro_total);
    printf("Puntos fuera del circulo: %ld\n", puntos_fuera_total);
    printf("Estimacion de PI: %.6f\n", pi_estimado);
    printf("Tiempo de ejecucion: %f segundos\n", tiempo);

    return 0;
}