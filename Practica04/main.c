#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <stdatomic.h>
#include <time.h>

#define TOTAL_PUNTOS 100000000LL


long long puntosDentro = 0;


atomic_int lock = 0;

static inline void lock_acquire(atomic_int *l) {
    int esperado = 0;
    
    while (!atomic_compare_exchange_weak(l, &esperado, 1)) {
        esperado = 0; 
    }
}

static inline void lock_release(atomic_int *l) {
    atomic_store(l, 0);
}


typedef struct {
    long long puntosPorHilo;
    unsigned int semilla;
} Args;


static inline unsigned int xorshift(unsigned int *state) {
    unsigned int x = *state;
    x ^= x << 13;
    x ^= x >> 17;
    x ^= x << 5;
    *state = x;
    return x;
}

static inline double rand_double(unsigned int *state) {
    return (xorshift(state) & 0xFFFFFF) / (double)0x1000000;
}


void *worker(void *arg) {
    Args *a = (Args *)arg;
    long long locales = 0;

    for (long long i = 0; i < a->puntosPorHilo; i++) {
        double x = rand_double(&a->semilla);
        double y = rand_double(&a->semilla);
        if (x * x + y * y <= 1.0) {
            locales++;
        }
    }

    
    lock_acquire(&lock);
    puntosDentro += locales;
    lock_release(&lock);

    return NULL;
}

int main(int argc, char *argv[]) {
    int numHilos = (argc > 1) ? atoi(argv[1]) : 4;

    pthread_t *hilos = malloc(sizeof(pthread_t) * numHilos);
    Args *args = malloc(sizeof(Args) * numHilos);

    long long base = TOTAL_PUNTOS / numHilos;
    long long resto = TOTAL_PUNTOS % numHilos;

    struct timespec t0, t1;
    clock_gettime(CLOCK_MONOTONIC, &t0);

    for (int i = 0; i < numHilos; i++) {
        args[i].puntosPorHilo = base + (i < resto ? 1 : 0);
        args[i].semilla = 123456789u + i * 987654321u;
        pthread_create(&hilos[i], NULL, worker, &args[i]);
    }

    for (int i = 0; i < numHilos; i++) {
        pthread_join(hilos[i], NULL);
    }

    clock_gettime(CLOCK_MONOTONIC, &t1);
    double tiempo = (t1.tv_sec - t0.tv_sec) + (t1.tv_nsec - t0.tv_nsec) / 1e9;

    double pi = 4.0 * (double)puntosDentro / (double)TOTAL_PUNTOS;

    printf("Hilos: %d | Puntos: %lld | Dentro: %lld | Pi: %.10f | Tiempo: %.4f s\n",
           numHilos, TOTAL_PUNTOS, puntosDentro, pi, tiempo);

    free(hilos);
    free(args);
    return 0;
}