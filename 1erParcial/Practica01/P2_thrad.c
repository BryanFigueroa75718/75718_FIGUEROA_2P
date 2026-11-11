#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>

int global_counter = 20;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *thread_routine(void *arg) {
    char buf[] = "New line \n";
    
    int fd = open("README.txt", O_WRONLY | O_APPEND | O_CREAT, 0644);
    if (fd == -1) return NULL;

    for (size_t i = 0; i < 1000; i++) {
        pthread_mutex_lock(&mutex);   
        global_counter++;             
        pthread_mutex_unlock(&mutex);

        write(fd, buf, sizeof(buf) - 1);
    }
    close(fd);
    return NULL;
}

void *thread_routine_two(void *arg) {
    char buf[] = "soy Sam Sulek ahuevo \n";

    int fd = open("README.txt", O_WRONLY | O_APPEND | O_CREAT, 0644);
    if (fd == -1) return NULL;

    for (size_t i = 0; i < 1000; i++) {
        pthread_mutex_lock(&mutex);   
        global_counter--;             
        pthread_mutex_unlock(&mutex); 

        write(fd, buf, sizeof(buf) - 1);
    }
    close(fd);
    return NULL;
}

int main(int argc, char const *argv[]) {
    pthread_t thread_one, thread_two;

    if (pthread_create(&thread_one, NULL, thread_routine, NULL) != 0) return -1;
    if (pthread_create(&thread_two, NULL, thread_routine_two, NULL) != 0) return -1;

    pthread_join(thread_one, NULL);
    pthread_join(thread_two, NULL);

    printf("Valor final de global_counter: %d\n", global_counter);

    pthread_mutex_destroy(&mutex);
    return 0;
}
