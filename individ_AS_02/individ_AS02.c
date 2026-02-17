#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>

#include "individ_AS02.h"

Queue *initializeQueue() {
    Queue *queue = malloc(sizeof(Queue));
    queue->first = NULL;
    queue->last = NULL;
    queue->size = 0;
    queue->max_size = 10;

    pthread_mutex_init(&queue->mutex, NULL);
    pthread_cond_init(&queue->full, NULL);
    pthread_cond_init(&queue->empty, NULL);

    return queue;
}

void freeQueue(Queue *queue) {
    pthread_mutex_destroy(&queue->mutex);
    pthread_cond_destroy(&queue->full);
    pthread_cond_destroy(&queue->empty);

    Node *current = queue->first;
    while (current) {
        Node *temp = current;
        current = current->next;
        free(temp);
    }
    free(queue);
}

void enqueue(Queue *queue, void *item) {

    pthread_mutex_lock(&queue->mutex);
    while (queue->size == queue->max_size) {
        pthread_cond_wait(&queue->full, &queue->mutex); // wait until the queue has space
    }

    Node *newNode = malloc(sizeof(Node));
    newNode->item = item;
    newNode->next = NULL;

    if(queue->last) { // attach new node to the last one
        queue->last->next = newNode;
    } else { // or just put it in if it is the first node
        queue->first = newNode;
    }
    queue->last = newNode;
    queue->size = queue->size + 1;
    free(newNode);

    pthread_cond_signal(&queue->empty); // let waiting threads know there is an item available
    pthread_mutex_unlock(&queue->mutex);
}

void *dequeue(Queue *queue) {

    pthread_mutex_lock(&queue->mutex);
    while (queue->size == 0) {
        pthread_cond_wait(&queue->empty, &queue->mutex); // wait until the queue has an item available
    }

    Node *firstNode = queue->first;
    queue->first = queue->first->next;
    if (queue->first == NULL) {
        queue->last = NULL;
    }
    queue->size = queue->size - 1;

    pthread_cond_signal(&queue->full); // let waiting threads know there is space available
    pthread_mutex_unlock(&queue->mutex);

    return firstNode->item;
}

// returns the number of items in the queue
int queue_size(Queue *queue) {
    pthread_mutex_lock(&queue->mutex);
    int size = queue->size;
    pthread_mutex_unlock(&queue->mutex);
    return size;
} 
