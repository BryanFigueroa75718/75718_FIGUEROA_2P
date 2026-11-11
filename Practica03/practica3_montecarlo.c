/*
    Práctica 3: Cálculo de π con hilos y semáforos
    Autor: Pimienta Díaz Ángela Alicia 69875
    Descripción: Implementación literal del pseudocódigo de MonteCarlo Total de puntos: 1000000
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>

#define TOTAL_PUNTOS 1000000

sem_t semaforo;
int puntosDentro = 0; 
int numHilos = 0;

void *monteCarlo(void *arg) {
    int id = *((int*)arg);
    int puntosPorHilo = TOTAL_PUNTOS / numHilos;
    unsigned int seed = time(NULL) ^ (id * 1234567); // semilla única por hilo

    for (int i = 0; i < puntosPorHilo; i++) {
        double x = ((double)rand_r(&seed) / RAND_MAX) * 2.0 - 1.0;
        double y = ((double)rand_r(&seed) / RAND_MAX) * 2.0 - 1.0;

        if ((x * x) + (y * y) <= 1.0) {
            sem_wait(&semaforo);
            puntosDentro++;
            sem_post(&semaforo);
        }
    }
    return NULL;
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("Uso: %s <num_hilos>\n", argv[0]);
        return 1;
    }

    numHilos = atoi(argv[1]);
    if (numHilos <= 0) {
        printf("El número de hilos debe ser mayor a 0.\n");
        return 1;
    }

    pthread_t *hilos = malloc(numHilos * sizeof(pthread_t));
    int *ids = malloc(numHilos * sizeof(int));

    sem_init(&semaforo, 0, 1);

    struct timespec inicio, fin;
    clock_gettime(CLOCK_MONOTONIC, &inicio);

    for (int i = 0; i < numHilos; i++) {
        ids[i] = i;
        pthread_create(&hilos[i], NULL, monteCarlo, &ids[i]);
    }

    for (int i = 0; i < numHilos; i++) {
        pthread_join(hilos[i], NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &fin);

    double tiempo = (fin.tv_sec - inicio.tv_sec) +
                    (fin.tv_nsec - inicio.tv_nsec) / 1e9;
    double pi = 4.0 * puntosDentro / TOTAL_PUNTOS;

    printf("Método Monte Carlo con hilos y semáforos\n");
    printf("Hilos: %d\n", numHilos);
    printf("Puntos totales: %d\n", TOTAL_PUNTOS);
    printf("Puntos dentro del círculo: %d\n", puntosDentro);
    printf("Valor aproximado de PI: %lf\n", pi);
    printf("Tiempo de ejecución: %f segundos\n", tiempo);

    sem_destroy(&semaforo);
    free(hilos);
    free(ids);
    return 0;
}