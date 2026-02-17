#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

#include "individ_AS02.h"

void *enqueue_thread(void *arg) {
    Queue *queue = (Queue *)arg;
    for (int i = 0; i < 5; i++) {
        int *item = malloc(sizeof(int));
        *item = (rand() % 90) + 10; // randomly generate a two-digit number
        printf("Thread A. Enqueuing item: %d\n", *item);
        enqueue(queue, item);
        sleep(1);
    }
    return NULL;
}

void *dequeue_thread(void *arg) {
    Queue *queue = (Queue *)arg;
    for (int i = 0; i < 5; i++) {
        void *item = dequeue(queue);
        if (item) {
            printf("Thread B. Dequeued item: %d\n", *(int *)item);
            free(item);
        } else {
            printf("Thread B. Queue was empty.\n");
        }
        sleep(2);
    }
    return NULL;
}

int main() {
    pthread_t thread1, thread2;
    int iret1, iret2;
    Queue *queue = initializeQueue();

    // Create threads to enqueue and dequeue
    iret1 = pthread_create(&thread1, NULL, enqueue_thread, (void *)queue);
    iret2 = pthread_create(&thread2, NULL, dequeue_thread, (void *)queue);

    // Wait for both threads to finish
    pthread_join(thread1, NULL);
    pthread_join(thread2, NULL);

    freeQueue(queue);
    return 0;
}
