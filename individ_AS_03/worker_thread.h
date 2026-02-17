#ifndef WORKER_THREAD_H
#define WORKER_THREAD_H

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#include "thread_pool.h"
#include "task.h"

void *worker_thread(void *arg);
void cleanup_function(void *arg);

#endif
