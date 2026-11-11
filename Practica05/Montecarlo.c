// Alan Campos Suarez 76750
// Practica: Medicion de Speedup, Eficiencia y Overhead en Monte Carlo
// Fecha: 09/09/2026

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

long long totalPuntos = 100000000;   // 100 millones de puntos (misma carga en las 3 ejecuciones)
int numHilos = 1;                    // se asigna por linea de comandos
long long puntosDentro = 0;          // contador global compartido

pthread_mutex_t lock;

// Genera un numero aleatorio entre min y max usando rand_r (thread-safe)
double randomEntre(double min, double max, unsigned int *seed) {
    return min + ((double)rand_r(seed) / RAND_MAX) * (max - min);
}

// Funcion que ejecuta cada hilo
void *hiloTrabajo(void *arg) {
    long long i;
    double x, y;
    long long localesDentro = 0;

    unsigned int seed = (unsigned int)time(NULL) ^ (unsigned int)pthread_self();

    long long puntosPorHilo = totalPuntos / numHilos;

    for (i = 0; i < puntosPorHilo; i++) {
        x = randomEntre(-1, 1, &seed);
        y = randomEntre(-1, 1, &seed);

        if (x * x + y * y <= 1) {
            localesDentro++;
        }
    }

    pthread_mutex_lock(&lock);
    puntosDentro += localesDentro;
    pthread_mutex_unlock(&lock);

    return NULL;
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Uso: %s <numero_de_hilos>\n", argv[0]);
        return 1;
    }

    numHilos = atoi(argv[1]);
    if (numHilos <= 0) {
        printf("El numero de hilos debe ser mayor a 0\n");
        return 1;
    }

    pthread_t *hilos = malloc(sizeof(pthread_t) * numHilos);
    if (hilos == NULL) {
        printf("Error al reservar memoria\n");
        return 1;
    }

    pthread_mutex_init(&lock, NULL);

    struct timespec inicio, fin;
    clock_gettime(CLOCK_MONOTONIC, &inicio);

    for (int i = 0; i < numHilos; i++) {
        pthread_create(&hilos[i], NULL, hiloTrabajo, NULL);
    }

    for (int i = 0; i < numHilos; i++) {
        pthread_join(hilos[i], NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &fin);

    double tiempo = (fin.tv_sec - inicio.tv_sec) +
                    (fin.tv_nsec - inicio.tv_nsec) / 1e9;

    pthread_mutex_destroy(&lock);
    free(hilos);

    double piEstimado = 4.0 * puntosDentro / totalPuntos;

    printf("Hilos: %d\n", numHilos);
    printf("Puntos dentro: %lld\n", puntosDentro);
    printf("Total puntos: %lld\n", totalPuntos);
    printf("PI estimado: %f\n", piEstimado);
    printf("Tiempo: %.6f segundos\n", tiempo);

    return 0;
}