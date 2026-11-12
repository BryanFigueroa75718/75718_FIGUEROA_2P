#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <string.h>


int global_counter = 20;


pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;


int use_mutex = 0; 


void *thread_routine(void *arg) {
    for (size_t i = 0; i < 1000; i++) {
        if (use_mutex) {
            pthread_mutex_lock(&mutex);
            global_counter++;
            pthread_mutex_unlock(&mutex);
        } else {
            global_counter++;
        }
    }
    return NULL;
}


void *thread_routine_two(void *arg) {
    for (size_t i = 0; i < 1000; i++) {
        if (use_mutex) {
            pthread_mutex_lock(&mutex);
            global_counter--;
            pthread_mutex_unlock(&mutex);
        } else {
            global_counter--; 
        }
    }
    return NULL;
}

int main(int argc, char *argv[]) {
    
    if (argc > 1 && strcmp(argv[1], "mutex") == 0) {
        use_mutex = 1;
        printf("Modo: CON Sincronización (Mutex)\n");
    } else {
        use_mutex = 0;
        printf("Modo: SIN Sincronización (Race Condition)\n");
    }

    pthread_t thread1, thread2;

    
    if (pthread_create(&thread1, NULL, thread_routine, NULL) != 0) {
        perror("Error creando hilo 1");
        return 1;
    }
    if (pthread_create(&thread2, NULL, thread_routine_two, NULL) != 0) {
        perror("Error creando hilo 2");
        return 1;
    }

    
    pthread_join(thread1, NULL);
    pthread_join(thread2, NULL);

    
    
    printf("Valor final de global_counter: %d\n", global_counter);
    printf("Valor esperado: 20\n");

    return 0;
}