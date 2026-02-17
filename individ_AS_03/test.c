#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "thread_pool.h"

void *message(void *arg) {
    char *message = (char *)arg;
    return (void *) message;
}

int main() {
    printf("Begin test on main thread: %lu\n", (unsigned long)pthread_self());
    ThreadPool *pool = ThreadPool_create(3, 3);
    printf("ThreadPool created.\n");

    char *str1 = "Hello";
    Task *task1 = Task_create(&message, (void *) str1);
    printf("Task1 created.\n");

    char *str2 = "Hola";
    Task *task2 = Task_create(&message, (void *) str2);
    printf("Task2 created.\n");

    char *str3 = "Bonjour";
    Task *task3 = Task_create(&message, (void *) str3);
    printf("Task3 created.\n");

    //for (int i = 0; i <= 3; i++) { // you can test multiple executions of the same task, if you would like
        ThreadPool_execute(pool, task1);
        ThreadPool_execute(pool, task2);
        ThreadPool_execute(pool, task3);
    //}

    printf("Sleeping: %lu\n", (unsigned long)pthread_self());
    sleep(1);

    ThreadPool_stop(pool);
    printf("ThreadPool stopped: %lu\n", (unsigned long)pthread_self());

    ThreadPool_destroy(pool);
    printf("ThreadPool destroyed: %lu\n", (unsigned long)pthread_self());

    Task_destroy(task1);
    printf("Task1 destroyed.\n");
    Task_destroy(task2);
    printf("Task2 destroyed.\n");
    Task_destroy(task3);
    printf("Task3 destroyed.\n");

    printf("Test complete.\n");
    
}