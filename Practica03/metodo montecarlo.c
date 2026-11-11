//Alan Campos Suarez 76750
//METODO MONTECARLO CALCULO DE PI
//09/09/2026

#include <stdio.h>      
#include <stdlib.h>     
#include <pthread.h>     

int totalPuntos = 1000000;  // cantidad total de puntos a generar en la simulacion
int numHilos = 4;           // cantidad de hilos que van a trabajar en paralelo
int puntosDentro = 0;       // contador global de puntos que cayeron dentro del circulo (variable compartida)

pthread_mutex_t lock;       // declara el mutex que va a proteger a puntosDentro

// genera un numero aleatorio entre min y max
double randomEntre(double min, double max) {
    return min + ((double)rand() / RAND_MAX) * (max - min);  // formula 

// funcion que ejecuta cada hilo
void *hiloTrabajo(void *arg) {
    int i;                          // contador del ciclo, local a cada hilo
    double x, y;                    // coordenadas del punto, locales a cada hilo

    for (i = 0; i < totalPuntos / numHilos; i++) {  // cada hilo repite su parte proporcional del total
        x = randomEntre(-1, 1);     // cada hilo genera sus propios puntos
        y = randomEntre(-1, 1);     // (no comparte los de otro hilo)

        if (x * x + y * y <= 1) {           // verifica si el punto cae dentro del circulo unitario
            pthread_mutex_lock(&lock);      // bloquea el mutex antes de tocar la variable compartida
            puntosDentro++;                 // incrementa el contador global de puntos dentro
            pthread_mutex_unlock(&lock);    // libera el mutex para que otro hilo pueda entrar
        }
    }

    return NULL;    // el hilo termina y no devuelve ningun valor
}

int main() {
    pthread_t hilos[numHilos];    // arreglo que va a guardar los identificadores de los hilos

    pthread_mutex_init(&lock, NULL);   // inicializa el mutex antes de usarlo

    // se crean los hilos
    for (int i = 0; i < numHilos; i++) {                          // recorre la cantidad de hilos a crear
        pthread_create(&hilos[i], NULL, hiloTrabajo, NULL);       // crea el hilo i y le asigna la funcion hiloTrabajo
    }

    // se espera a que todos terminen
    for (int i = 0; i < numHilos; i++) {    // recorre todos los hilos creados
        pthread_join(hilos[i], NULL);       // el hilo principal espera a que el hilo i termine
    }

    pthread_mutex_destroy(&lock);   // libera los recursos del mutex, ya no se necesita

    double piEstimado = 4.0 * puntosDentro / totalPuntos;   // aplica la formula de Monte Carlo para estimar pi

    printf("Puntos dentro: %d\n", puntosDentro);      
    printf("Total puntos: %d\n", totalPuntos);         
    printf("PI estimado: %f\n", piEstimado);           

    return 0;    
}