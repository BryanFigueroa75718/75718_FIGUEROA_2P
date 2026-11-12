#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <sys/time.h>


int totalPuntos = 1000000;
int numHilos;
int puntosDentro = 0;
sem_t semaforo;      


typedef struct {
    int id_hilo;
    int puntos_por_hilo;
} DatosHilo;


void* calcular_pi(void* arg) {
    DatosHilo* datos = (DatosHilo*)arg;
    int puntos_locales = 0;
    
    
    unsigned int seed = time(NULL) ^ (datos->id_hilo * 12345);
    
    for (int i = 0; i < datos->puntos_por_hilo; i++) {
    
    
        double x = ((double)rand_r(&seed) / RAND_MAX) * 2.0 - 1.0;
        double y = ((double)rand_r(&seed) / RAND_MAX) * 2.0 - 1.0;
        
    
        if ((x * x + y * y) <= 1.0) {
            puntos_locales++;
        }
    }
    
    
    
    sem_wait(&semaforo);
    puntosDentro += puntos_locales;
    sem_post(&semaforo);
    
    free(arg);
    pthread_exit(NULL);
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        printf("Uso: %s <numero_de_hilos>\n", argv[0]);
        return 1;
    }
    
    numHilos = atoi(argv[1]);
    if (numHilos <= 0) {
        printf("El número de hilos debe ser mayor a 0.\n");
        return 1;
    }

    
    sem_init(&semaforo, 0, 1);

    pthread_t hilos[numHilos];
    int puntos_por_hilo = totalPuntos / numHilos;
    int resto = totalPuntos % numHilos;

    
    struct timeval inicio, fin;
    gettimeofday(&inicio, NULL);

    
    for (int i = 0; i < numHilos; i++) {
        DatosHilo* datos = (DatosHilo*)malloc(sizeof(DatosHilo));
        datos->id_hilo = i;
    
        datos->puntos_por_hilo = puntos_por_hilo + (i < resto ? 1 : 0);
        
        if (pthread_create(&hilos[i], NULL, calcular_pi, (void*)datos) != 0) {
            perror("Error creando hilo");
            return 1;
        }
    }

    
    for (int i = 0; i < numHilos; i++) {
        pthread_join(hilos[i], NULL);
    }

    
    gettimeofday(&fin, NULL);
    double tiempo_total = (fin.tv_sec - inicio.tv_sec) + (fin.tv_usec - inicio.tv_usec) / 1000000.0;

    
    double pi = 4.0 * puntosDentro / totalPuntos;

    
    printf("----------------------------------------\n");
    printf("Hilos utilizados: %d\n", numHilos);
    printf("Puntos totales:   %d\n", totalPuntos);
    printf("Puntos dentro:    %d\n", puntosDentro);
    printf("Valor de Pi:      %.6f\n", pi);
    printf("Tiempo ejecución: %.6f segundos\n", tiempo_total);
    printf("----------------------------------------\n");

    
    sem_destroy(&semaforo);

    return 0;
}