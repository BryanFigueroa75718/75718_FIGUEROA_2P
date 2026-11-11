#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>

int global_counter = 20;
int shared_line_num = 1;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *thread_routine_1(void *arg){
    int fd;
    char buf[64];

    printf("Starting thread...\n");

    pthread_mutex_lock(&mutex);
    for(int i=0; i<1000; i++)
    {
        global_counter++;

        sprintf(buf, "Hilo 1 - Linea %d\n", shared_line_num++);
        fd = open("/home/alancs/CyP_2026/readme.txt", O_WRONLY | O_APPEND);
        write(fd, buf, strlen(buf));
        close(fd);
    }
    pthread_mutex_unlock(&mutex);
}

void *thread_routine_2(void *arg){
    int fd;
    char buf[64];

    printf("Starting second thread...\n");

    pthread_mutex_lock(&mutex);
    for(int i=0; i<1000; i++)
    {
        global_counter--;

        sprintf(buf, "Hilo 2 - Linea %d\n", shared_line_num++);
        fd = open("/home/alancs/CyP_2026/readme.txt", O_WRONLY | O_APPEND);
        write(fd, buf, strlen(buf));
        close(fd);
    }
    pthread_mutex_unlock(&mutex);
}

int main(int argc, char const *argv[])
{
    pthread_t thread_one;
    pthread_t thread_two;

    if(0!=pthread_create(&thread_one, NULL, thread_routine_1, NULL))
        return 1;

    if(0!=pthread_create(&thread_two, NULL, thread_routine_2, NULL))
        return 1;

    pthread_join(thread_one, NULL);
    pthread_join(thread_two, NULL);

    printf("Valor final de global_counter: %d\n", global_counter);

    return 0;
}