#include "task.h"

Task *Task_create(void* (*run)(void*), void *args) {
    Task *task = malloc(sizeof(Task));
    task->state = 0;
    task->run = run;
    task->args = args;
    task->returnResult = NULL;
    return task;
}

void Task_destroy(Task *task) {
    free(task);
}