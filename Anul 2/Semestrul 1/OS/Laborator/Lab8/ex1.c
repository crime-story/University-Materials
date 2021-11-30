#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <pthread.h>
#include <time.h>
#include <unistd.h>

#define MAX_RESOURCES 100
#define MAX_THREADS 12

int available_resources = MAX_RESOURCES;

pthread_mutex_t mtx;

int decrease_count(int count) {
    pthread_mutex_lock(&mtx); // spunem sa nu mai intre alt thread, sa astepte

    if (available_resources < count) { // verificam daca avem destul resurse, daca nu eliberam mutexul cu unlock
        pthread_mutex_unlock(&mtx);
        return -1;
    }
    else { // daca avem destule resurse le luam, dupa care le eliberam
        available_resources -= count;
        printf("Got %d resources %d remaining\n", count, available_resources);
        pthread_mutex_unlock(&mtx);
    }
    return 0;
}

int increase_count(int count) {
    pthread_mutex_lock(&mtx); // spunem sa nu mai intre alt thread, sa astepte

    available_resources += count; // ii dam inapoi resursele
    printf("Released %d resources %d remaining\n", count, available_resources);

    pthread_mutex_unlock(&mtx); // eliberam mutexul cu unlock
    return 0;
}

void* myThread(void* v) {
    int count = *((int*)v);
    while(decrease_count(count)); // cat timp nu se poate accesa resursa
    sleep(1);
    increase_count(count);

    free(v);
    return NULL;
}

int main() {
    srand(time(NULL));

    printf("MAX_RESOURCES=%d\n", MAX_RESOURCES);

    if (pthread_mutex_init(&mtx, NULL)) {
        perror(NULL);
        return errno;
    }

    pthread_t* thrs = malloc(MAX_THREADS * sizeof(pthread_t));

    for (int i = 0; i < MAX_THREADS; i++ ) {
        int *random = malloc(sizeof(int));
        *random = rand() % MAX_RESOURCES / 3 + 1;
        if (pthread_create(&thrs[i], NULL, myThread, random)) { // initializam thread-ul
            perror(NULL);
            return errno;
        }
    }

    for (int i = 0; i < MAX_THREADS; i++) {
        if (pthread_join(thrs[i], NULL)) {
            perror(NULL);
            return errno;
        }
    }

    free(thrs);
    pthread_mutex_destroy(&mtx); // distrugem mutexul
    return 0;
}