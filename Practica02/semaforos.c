/*
    Programa:Semáforos-Autor: Angela Alicia Pimienta Diaz-08/09/2026
    En este podemos ver la comunicación entre dos hilos, un hilo representa al cocinero que es quien prepara comida y avisa
    mediante un semáforo cuando hay una orden lista, mientras que el segundo hilo representa al mesero quien espera la señal
    para poder servir la comida.
*/


#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <unistd.h>


#define NR_LOOP 10   // Número de veces que se repetirá la preparación


// las funciones que serán ejecutadas por los hilos
static void *cocinar(void *arg);
static void *servir(void *arg);


// esta es la variable que comparten los hilos
static int counter = 0; // Contador de comidas preparadas


// Semáforo utilizado para coordinar la comunicación entre el cocinero y el mesero
sem_t sem1;



int main(void)
{

    pthread_t cocinero;
    pthread_t mesero;


    /*
        A qui se hace la Inicialización del semáforo.
        El valor inicial es 0 porque al inicio
        no existe comida disponible.
    */
    sem_init(&sem1, 0, 0);


    // Creación de los hilos: cocinero ejecuta la función cocinar y  el mesero ejecuta la función servir
    pthread_create(&cocinero, NULL, cocinar, NULL);
    pthread_create(&mesero, NULL, servir, NULL);



    /*
        Espera a que ambos hilos terminen antes
        de finalizar el programa principal.
    */
    pthread_join(cocinero, NULL);
    pthread_join(mesero, NULL);



    printf("\nContador final: %d\n", counter);


    // Liberación del recurso semáforo
    sem_destroy(&sem1);


    return 0;
}


   // la función cocinar recibe un puntero genérico utilizado por pthread y devuelve NULL al finalizar.

static void *cocinar(void *arg)
{

    for(int i = 0; i < NR_LOOP; i++)
    {
        printf("COCINERO: Comida preparada\n");


        // Incrementa la cantidad de comidas preparadas
        counter++;

            //Envía una señal al mesero indicando que existe una comida disponible.

        sem_post(&sem1);
        sleep(1);
    }


    return NULL;
}



//  la función servir recibe un puntero genérico utilizado por pthread y devuelve NULL al finalizar su funcion es esperar la señal del semáforo antes de servir.
static void *servir(void *arg)
{

    for(int i = 0; i < NR_LOOP; i++)
    {

        // Bloquea el hilo hasta que el cocinero haya preparado una comida.
        sem_wait(&sem1);


        printf("MESERO: Comida servida\n");

    }


    return NULL;
}