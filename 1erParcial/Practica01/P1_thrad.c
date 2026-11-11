#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>

void *thread_routine(void *arg) {
    int num_line = *((int*)arg);
    char buf[] = "New line Cesar Raul \n";

    printf("Starting thread 1\n");

    int fd = open("README.txt", O_WRONLY | O_APPEND | O_CREAT, 0644);
    if (fd != -1) {
        for (int i = 0; i < num_line; i++) {
            write(fd, buf, sizeof(buf) - 1);
        }
        close(fd);
    }
    return NULL;
}

void *thread_routines(void *arg) {
    int num_line = *((int*)arg);
    char buf[] = "Soy Sam Sulek \n";

    printf("Starting thread 2\n");

    int fd = open("README.txt", O_WRONLY | O_APPEND | O_CREAT, 0644);
    if (fd != -1) {
        for (int i = 0; i < num_line; i++) {
            write(fd, buf, sizeof(buf) - 1);
        }
        close(fd);
    }
    return NULL;
}

int main(int argc, char const *argv[]) {
    if (argc < 2) {
        printf("Uso: %s <numero_de_lineas>\n", argv[0]);
        return 1;
    }

    pthread_t thread_one, thread_two;
    int counter = atoi(argv[1]);

    if (pthread_create(&thread_one, NULL, thread_routine, &counter) != 0)
        return -1;
    if (pthread_create(&thread_two, NULL, thread_routines, &counter) != 0)
        return -1;

    pthread_join(thread_one, NULL);
    pthread_join(thread_two, NULL);

    return 0;
}
