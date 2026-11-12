#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

#define TOTAL_PUNTOS 100000000

int puntosDentro = 0; 
int numHilos;

volatile int *choosing;
volatile int *ticket;

void lock(int id) {
    choosing[id] = 1;
    __sync_synchronize();

    int max_ticket = 0;
    for (int i = 0; i < numHilos; i++) {
        if (ticket[i] > max_ticket) {
            max_ticket = ticket[i];
        }
    }
    ticket[id] = max_ticket + 1;
    
    __sync_synchronize();
    choosing[id] = 0;
    __sync_synchronize();

    for (int j = 0; j < numHilos; j++) {
        while (choosing[j]) { 
        }
        while (ticket[j] != 0 && (ticket[j] < ticket[id] || (ticket[j] == ticket[id] && j < id))) {

        }
    }
}

void unlock(int id) {
    __sync_synchronize();
    ticket[id] = 0;
}

void* calcular_pi(void* arg) {
    long id_hilo = (long)arg;
    int iteraciones = TOTAL_PUNTOS / numHilos;
    unsigned int seed = (unsigned int)pthread_self(); 

    for (int i = 0; i < iteraciones; i++) {
        double x = (double)rand_r(&seed) / RAND_MAX * 2.0 - 1.0;
        double y = (double)rand_r(&seed) / RAND_MAX * 2.0 - 1.0;

        if (x * x + y * y <= 1.0) {
            lock(id_hilo);
            puntosDentro++;
            unlock(id_hilo);
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
    
    choosing = (volatile int*)calloc(numHilos, sizeof(int));
    ticket = (volatile int*)calloc(numHilos, sizeof(int));

    struct timeval start, end;
    gettimeofday(&start, NULL);

    for (long i = 0; i < numHilos; i++) {
        pthread_create(&hilos[i], NULL, calcular_pi, (void*)i);
    }

    for (int i = 0; i < numHilos; i++) {
        pthread_join(hilos[i], NULL);
    }

    gettimeofday(&end, NULL);
    double tiempo_ejecucion = (end.tv_sec - start.tv_sec) + (end.tv_usec - start.tv_usec) / 1000000.0;

    double pi = 4.0 * puntosDentro / TOTAL_PUNTOS;

    printf("Hilos: %d | Puntos: %d | Pi: %f | Tiempo: %f seg\n", numHilos, TOTAL_PUNTOS, pi, tiempo_ejecucion);

    free((void*)choosing);
    free((void*)ticket);

    return 0;
}