#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>

#include "blocking_queue.h"

Queue *initializeQueue(int max_size) {
    Queue *queue = malloc(sizeof(Queue));
    queue->first = NULL;
    queue->last = NULL;
    queue->size = 0;
    queue->max_size = max_size;

    pthread_mutex_init(&queue->queue_mutex, NULL);
    pthread_cond_init(&queue->full, NULL);
    pthread_cond_init(&queue->empty, NULL);

    return queue;
}

void freeQueue(Queue *queue) {
    pthread_mutex_destroy(&queue->queue_mutex);
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

    pthread_mutex_lock(&queue->queue_mutex);
    while (queue->size == queue->max_size) {
        pthread_cond_wait(&queue->full, &queue->queue_mutex);
    }

    Node *newNode = malloc(sizeof(Node));
    newNode->item = item;
    newNode->next = NULL;

    if(queue->last) {
        queue->last->next = newNode;
    } else { // the queue is empty
        queue->first = newNode;
    }
    queue->last = newNode;
    queue->size = queue->size + 1;

    pthread_cond_signal(&queue->empty); // let waiting threads know there is an item available
    pthread_mutex_unlock(&queue->queue_mutex); // unlock because we are done modifying the queue
}

void *dequeue(Queue *queue) {

    pthread_mutex_lock(&queue->queue_mutex);

    while (queue->size == 0) {
        pthread_cond_wait(&queue->empty, &queue->queue_mutex);
    }

    Node *firstNode = queue->first;

    if (firstNode != NULL) {
        queue->first = firstNode->next;
        if (queue->first == NULL) {
            queue->last = NULL;
        }
        queue->size--;

        void *item = firstNode->item;
        free(firstNode);

        pthread_cond_signal(&queue->full);
        pthread_mutex_unlock(&queue->queue_mutex);

        return item;
        
    } else {
        pthread_mutex_unlock(&queue->queue_mutex);
        return NULL;
    }
}

// returns the number of items in the queue
int queue_size(Queue *queue) {
    pthread_mutex_lock(&queue->queue_mutex);
    int size = queue->size;
    pthread_mutex_unlock(&queue->queue_mutex);
    return size;
} 