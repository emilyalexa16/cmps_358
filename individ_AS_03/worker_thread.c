#include "worker_thread.h"

void *worker_thread(void *arg) {
    ThreadPool *pool = (ThreadPool *)arg;
    printf("Thread begins: %lu\n", (unsigned long)pthread_self());

    pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
    pthread_setcanceltype(PTHREAD_CANCEL_DEFERRED, NULL);

    pthread_cleanup_push(cleanup_function, pool); // prep the cleanup

    while (1) {
        printf("Checking for cancel request: %lu\n", (unsigned long)pthread_self());
        Task *task = (Task *)dequeue(pool->queue);
        task->state = 2; // task state is set to running
        task->returnResult = task->run(task->args);
        if (task->returnResult) {
            printf("Return result: %s\n", (char *)task->returnResult);
        } else {
            printf("Return result: (null)\n");
        }
        task->state = 3; // task state is set to completed
    }

    pthread_cleanup_pop(1); // cleanup if cancellation request has been made

    return NULL;
}

void cleanup_function(void *arg) {
    printf("Cleanup complete.");
}