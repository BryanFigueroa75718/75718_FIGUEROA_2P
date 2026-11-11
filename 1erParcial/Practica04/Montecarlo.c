// Tarea: Algoritmo de Exclusión Mutua con Monte Carlo (Test-and-Set)
// Autor: Cesar Raul Ramirez Cab 69967
// Fecha: 27/09/2026

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <stdint.h>
#include <time.h>

#define TOTAL_PUNTOS 100000000LL 


#ifndef rand_r
static inline int rand_r(unsigned int *seed) {
    *seed = *seed * 1103515245 + 12345;
    return (int)((*seed / 65536) % 32768);
}
#endif


long long puntosDentro = 0;
volatile int lock_flag = 0;
int num_hilos = 4;

void lock_test_and_set(volatile int *lock) {
    while (__atomic_test_and_set(lock, __ATOMIC_ACQUIRE)) {
       
    }
}

void unlock_test_and_set(volatile int *lock) {
    __atomic_clear(lock, __ATOMIC_RELEASE);
}

void* calcular_pi(void* arg) {
    uintptr_t id_hilo = (uintptr_t)arg;

    long long puntos_por_hilo = TOTAL_PUNTOS / num_hilos;
    long long inicio = id_hilo * puntos_por_hilo;
    long long fin = (id_hilo == (uintptr_t)(num_hilos - 1)) ? TOTAL_PUNTOS : (inicio + puntos_por_hilo);

    unsigned int seed = (unsigned int)time(NULL) ^ (unsigned int)pthread_self() ^ (unsigned int)id_hilo;

    for (long long i = inicio; i < fin; i++) {
        double x = ((double)rand_r(&seed) / 32767.0) * 2.0 - 1.0;
        double y = ((double)rand_r(&seed) / 32767.0) * 2.0 - 1.0;

        if (x * x + y * y <= 1.0) {
      
            lock_test_and_set(&lock_flag);
            puntosDentro++;
            unlock_test_and_set(&lock_flag);
        }
    }

    return NULL;
}

int main(int argc, char* argv[]) {
    if (argc > 1) {
        num_hilos = atoi(argv[1]);
    }

    pthread_t *hilos = malloc(sizeof(pthread_t) * num_hilos);
    struct timespec inicio, fin;

  
    clock_gettime(CLOCK_MONOTONIC, &inicio);

    
    for (intptr_t i = 0; i < num_hilos; i++) {
        if (pthread_create(&hilos[i], NULL, calcular_pi, (void*)i) != 0) {
            perror("Error al crear el hilo");
            free(hilos);
            return 1;
        }
    }

    for (int i = 0; i < num_hilos; i++) {
        pthread_join(hilos[i], NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &fin);
    double tiempo = (fin.tv_sec - inicio.tv_sec) + 
                    (fin.tv_nsec - inicio.tv_nsec) / 1e9;

    double pi_estimado = 4.0 * (double)puntosDentro / (double)TOTAL_PUNTOS;

    printf("\n--- RESULTADO FINAL ---\n");
    printf("Algoritmo: Exclusi%cn Mutua con Test-and-Set\n", 162);
    printf("Numero de hilos: %d\n", num_hilos);
    printf("Puntos totales: %lld\n", TOTAL_PUNTOS);
    printf("Puntos dentro del circulo: %lld\n", puntosDentro);
    printf("Estimacion de PI: %.6f\n", pi_estimado);
    printf("Tiempo de ejecucion: %f segundos\n", tiempo);

    free(hilos);
    return 0;
}