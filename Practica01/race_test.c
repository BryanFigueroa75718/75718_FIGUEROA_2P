#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>

int global_counter = 20;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

void *thread_routine(void *arg){
    int num_line = *((int*)arg);
    int fd;
    char buf[100];
    
    printf("Starting thread...\n");
    
    for (int i = 0; i < num_line; i++){
        pthread_mutex_lock(&mutex);
        
        global_counter++;
        snprintf(buf, sizeof(buf), "Thread1 - Contador: %d\n", global_counter);
        
        fd = open("/home/pim/poing/readme.txt", O_WRONLY | O_APPEND | O_CREAT, 0644);
        write(fd, buf, strlen(buf));
        close(fd);
        
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

void *thread_routine_two(void *arg){
    int num_line = *((int*)arg);
    int fd;
    char buf[100];
    
    printf("Starting thread two...\n");
    
    for (int i = 0; i < num_line; i++){
        pthread_mutex_lock(&mutex);
        
        global_counter--;
        snprintf(buf, sizeof(buf), "Thread2 - Contador: %d\n", global_counter);
        
        fd = open("/home/pim/poing/readme.txt", O_WRONLY | O_APPEND | O_CREAT, 0644);
        write(fd, buf, strlen(buf));
        close(fd);
        
        pthread_mutex_unlock(&mutex);
    }
    return NULL;
}

int main(int argc, char const *argv[]){
    
    if (argc < 2) {
        printf("Uso: %s <numero_de_lineas>\n", argv[0]);
        return -1;
    }
    
    int counter = 0;
    pthread_t thread_one;
    pthread_t thread_two;
    
    counter = atoi(argv[1]);
    
    if (0 != pthread_create(&thread_one, NULL, thread_routine, &counter))
        return -1;
    
    if (0 != pthread_create(&thread_two, NULL, thread_routine_two, &counter))
        return -1;
    
    pthread_join(thread_one, NULL);
    pthread_join(thread_two, NULL);
    
    return 0;
}