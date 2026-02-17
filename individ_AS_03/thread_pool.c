#include "thread_pool.h"

ThreadPool *ThreadPool_create(int nThreads, int capacityOfQueue) {

    ThreadPool *pool = malloc(sizeof(ThreadPool));

    pool->isStopped = 0;

    pool->capacityOfTaskQueue = capacityOfQueue;
    pool->queue = initializeQueue(capacityOfQueue);

    pool->nThreads = nThreads;

    pool->threads = malloc(sizeof(pthread_t) * pool->nThreads);
    for (int i = 0; i < pool->nThreads; i++) {
        pthread_create(&pool->threads[i], NULL, worker_thread, (void *)pool);
    }

    return pool;
}

Task *ThreadPool_execute(ThreadPool *threadPool, Task *task) {
    if (threadPool->isStopped != 0) {
        task->state = 4;
    } else {
        enqueue(threadPool->queue, task);
        task->state = 1;
    }
    return task;
}

void ThreadPool_stop(ThreadPool *threadPool) {

    threadPool->isStopped = 1; // initiate stopping

    pthread_mutex_lock(&threadPool->queue->queue_mutex);
    pthread_cond_broadcast(&threadPool->queue->empty); // Wake up any threads that are stuck waiting in dequeue()
    pthread_mutex_unlock(&threadPool->queue->queue_mutex);

    // Send a cancellation request to each thread, wait until it terminates
    for (int i = 0; i < threadPool->nThreads; i++) {
        printf("Requesting cancellation: %lu\n", (unsigned long)threadPool->threads[i]);
        pthread_cancel(threadPool->threads[i]);
        printf("Cancellation complete: %lu\n", (unsigned long)threadPool->threads[i]);
    }

    threadPool->isStopped = 2; // stopping is complete
}

int ThreadPool_destroy(ThreadPool *threadPool) {
    if (threadPool->isStopped == 0) { // threadPool is not stopped; do not destroy
        return 1; // destroy failed
    } else if (threadPool->isStopped == 1) {
        while (threadPool->isStopped != 2) { } // wait for threadPool to fully stop
        freeQueue(threadPool->queue); // free the queue
        free(threadPool->threads); // free the threads pointer
        free(threadPool); // free the threadPool itself
    }
    return 0; // destroy succeeded
}