#ifndef INDIVID_AS02_H
#define INDIVID_AS02_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>

typedef struct QUEUE_NODE {
    void *item;
    struct QUEUE_NODE *next;
} Node;

typedef struct BLOCKING_QUEUE {
    Node *first;
    Node *last;
    int max_size;
    int size;
    pthread_mutex_t mutex;
    pthread_cond_t full;
    pthread_cond_t empty;
} Queue;

Queue *initializeQueue();
void freeQueue(Queue *queue);

void enqueue(Queue *queue, void *item);
Node *dequeue(Queue *queue);
int size(Queue *queue);

#endif