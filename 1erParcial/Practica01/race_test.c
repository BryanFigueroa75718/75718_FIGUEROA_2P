
//Practica 01 - Sección Crítica -- Pedro Alejandro Chavez Rivero
#include <stdio.h>
#include <pthread.h>

int global_counter = 20;

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *thread_routine(void *arg) {
    for (size_t i = 0; i < 1000; i++) {
        pthread_mutex_lock(&mutex);
        global_counter++;
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

void *thread_routine_two(void *arg) {
    for (size_t i = 0; i < 1000; i++) {
        pthread_mutex_lock(&mutex);
        global_counter--;
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main() {
    pthread_t t1, t2;
    printf("===========================\n");
    printf("\n");
    printf("El valor inicial es: %d\n", global_counter);

    pthread_create(&t1, NULL, thread_routine, NULL);
    pthread_create(&t2, NULL, thread_routine_two, NULL);
    
    pthread_join(t1, NULL);
    pthread_join(t2, NULL);
    
    printf("El valor final es: %d\n", global_counter);
    printf("\n");
    printf("===========================\n");
    return 0;
}