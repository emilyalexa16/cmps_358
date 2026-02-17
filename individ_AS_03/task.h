#ifndef TASK_H
#define TASK_H

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct TASK {
    int state;
    void *(*run)(void *);
    void *args;
    void *returnResult;
} Task;

Task *Task_create(void* (*run)(void*), void *args);
void Task_destroy(Task *task);

#endif
