#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <pthread.h>
#include <semaphore.h>

#define NTHRS 6

pthread_mutex_t mtx;
sem_t sem;

int count, nr_threads;

void barrier_point() {
    pthread_mutex_lock(&mtx);
    count++;

    if (count == nr_threads) {
        pthread_mutex_unlock(&mtx);
		if (sem_post(&sem)) {
			perror(NULL);
			return errno;
		}
    }
    else {
        pthread_mutex_unlock(&mtx);
        if (sem_wait(&sem)) { // face -
			perror(NULL);
			return errno;
		}
        if (sem_post(&sem)) { // face +
			perror(NULL);
			return errno;
		}
    }
}

void* tfun(void* v) {
    int *tid = (int*)v;

    printf("%d reached the barrier\n", *tid);
    barrier_point();
    printf("%d passed the barrier\n", *tid);

    free(tid);
    return NULL;
}

void init(int N) {
    nr_threads = N;
}

int main() {
    init(NTHRS); // initializam numarul de thread-uri folosind functia init asa cum se cere in cerinta

    printf("NTHRS=%d\n", nr_threads);

    if (pthread_mutex_init(&mtx, NULL)) {
        perror(NULL);
        return errno;
    }  

    if (sem_init(&sem, 0, 0)) {
        perror(NULL);
        return errno;
    }

    pthread_t* thrs = malloc(nr_threads * sizeof(pthread_t));
    
    for (int i = 0; i < nr_threads; i++) {
        int *id = malloc(sizeof(int));
        *id = i;
        if (pthread_create(&thrs[i], NULL, tfun, id)) {
            perror(NULL);
            return errno;
        }
    }

    for (int i = 0; i < nr_threads; i++) {
        if (pthread_join(thrs[i], NULL)) {
            perror(NULL);
            return errno;
        }
    }

    free(thrs);
    pthread_mutex_destroy(&mtx);
    sem_destroy(&sem);
    return 0;
}