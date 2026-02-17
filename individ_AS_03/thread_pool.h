#ifndef THREAD_POOL_H
#define THREAD_POOL_H

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "blocking_queue.h"
#include "task.h"
#include "worker_thread.h"

typedef struct THREAD_POOL {
    int isStopped; // 0 is false, 1 is being stopped, 2 is all threads terminated
    int capacityOfTaskQueue;
    Queue *queue;
    int nThreads;
    pthread_t *threads;
} ThreadPool;

ThreadPool *ThreadPool_create(int nThreads, int capacityOfQueue);
Task *ThreadPool_execute(ThreadPool *threadPool, Task *task);
void ThreadPool_stop(ThreadPool *threadPool);
int ThreadPool_destroy(ThreadPool *threadPool);

#endif
