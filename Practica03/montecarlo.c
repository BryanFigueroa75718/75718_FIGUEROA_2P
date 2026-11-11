//Montecarlo / Diego Valadez Almeyda - 09/09/2026
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>


#define NUM_HILOS 16

long long aciertos_totales = 0;

pthread_mutex_t mutex_aciertos;

typedef struct {
	long long puntos_lanzados;
	unsigned int semilla_local;
} DatosHilo;

int rand_r(unsigned int *seed) {
    *seed = *seed * 1103515245 + 12345;
    return (unsigned int)(*seed / 65536) % 32768;
}

void* calcular_puntos(void* arg){
	DatosHilo* datos = (DatosHilo*)arg;

	long long aciertos_locales = 0;

	for(long long i = 0; i < datos->puntos_lanzados; i++) {
		double x = ((double)rand_r(&datos->semilla_local) / RAND_MAX) * 2.0 - 1.0;
		double y = ((double)rand_r(&datos->semilla_local) / RAND_MAX) * 2.0 - 1.0;

		if ((x*x) + (y*y) <= 1.0) {
			aciertos_locales++;
		}
	}

	pthread_mutex_lock(&mutex_aciertos);

	aciertos_totales += aciertos_locales;

	pthread_mutex_unlock(&mutex_aciertos);

	pthread_exit(NULL);
}

int main() {

	clock_t inicio = clock();

	long long total_puntos = 1000000;
	long long puntos_hilo = total_puntos / NUM_HILOS;

	pthread_t hilos[NUM_HILOS];
	DatosHilo datos_hilos[NUM_HILOS];

	pthread_mutex_init(&mutex_aciertos, NULL);

	for(int i = 0; i < NUM_HILOS; i++) {
		datos_hilos[i].puntos_lanzados = puntos_hilo;
		datos_hilos[i].semilla_local = time(NULL) ^ i;
		pthread_create(&hilos[i], NULL, calcular_puntos, (void*)&datos_hilos[i]);
	}

	for(int i = 0; i< NUM_HILOS; i++) {
		pthread_join(hilos[i], NULL);
	}

	pthread_mutex_destroy(&mutex_aciertos);

	double pi_aprox = 4.0 * ((double)aciertos_totales / total_puntos);

	clock_t fin = clock();

	printf("Puntos totales = %lld (procesados en %d hilos con mutex)\n", total_puntos, NUM_HILOS);
	printf("Valor aproximado de Pi = %f\n", pi_aprox);

	double tiempo = (double)(fin - inicio) / CLOCKS_PER_SEC;
	printf("Tiempo de CPU: %f segundos\n", tiempo);

	return 0;
}

