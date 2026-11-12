#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <sys/time.h>

#define TOTAL_PUNTOS 1000000

int puntosDentro = 0;
int numHilos;
sem_t semaforo;

void* calcular_pi(void* arg) {
    int iteraciones = TOTAL_PUNTOS / numHilos;
    unsigned int seed = (unsigned int)pthread_self(); 

    for (int i = 0; i < iteraciones; i++) {
        double x = (double)rand_r(&seed) / RAND_MAX * 2.0 - 1.0;
        double y = (double)rand_r(&seed) / RAND_MAX * 2.0 - 1.0;

        if (x * x + y * y <= 1.0) {
            sem_wait(&semaforo); 
            puntosDentro++;
            sem_post(&semaforo); 
        }
    }
    return NULL;
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Uso: %s <num_hilos>\n", argv[0]);
        return 1;
    }

    numHilos = atoi(argv[1]);
    pthread_t hilos[numHilos];
    
    sem_init(&semaforo, 0, 1);

    struct timeval start, end;
    gettimeofday(&start, NULL);

    for (int i = 0; i < numHilos; i++) {
        pthread_create(&hilos[i], NULL, calcular_pi, NULL);
    }

    for (int i = 0; i < numHilos; i++) {
        pthread_join(hilos[i], NULL);
    }

    gettimeofday(&end, NULL);
    double tiempo_ejecucion = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;

    double pi = 4.0 * puntosDentro / TOTAL_PUNTOS;

    printf("Hilos: %d | Pi estimado: %f | Tiempo: %f segundos\n", numHilos, pi, tiempo_ejecucion);

    sem_destroy(&semaforo);
    return 0;
}