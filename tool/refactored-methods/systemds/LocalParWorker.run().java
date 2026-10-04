@Override
public void run() {
    int pool = setup();
    if (pool == -1) {
        return;
    }
    try {
        executeTasks();
    } finally {
        cleanup(pool);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private int setup() {
    int pool = -1;
    if (OptimizerUtils.isSparkExecutionMode() && SparkExecutionContext.isSparkContextCreated()) {
        SparkExecutionContext sec = (SparkExecutionContext) _ec;
        pool = sec.setThreadLocalSchedulerPool();
    }
    if (DMLScript.USE_ACCELERATOR) {
        try {
            _ec.getGPUContext(0).initializeThread();
        } catch (DMLRuntimeException e) {
            LOG.error("Error executing task because of failure in GPU backend: ", e);
            LOG.error("Stopping LocalParWorker.");
            return -1;
        }
    }
    ConfigurationManager.setLocalConfig(_cconf);
    return pool;
}

private void executeTasks() {
    Task lTask = null;
    try {
        while (!_stopped) {
            try {
                lTask = _taskQueue.dequeueTask();
                if (lTask == LocalTaskQueue.NO_MORE_TASKS) {
                    break;
                }
            } catch (Exception ex) {
                LOG.warn("Error reading from task queue: " + ex.getMessage());
                LOG.warn("Stopping LocalParWorker.");
                break;
            }
            boolean success = false;
            int retries = _max_retry;
            while (!success) {
                try {
                    executeTask(lTask);
                    success = true;
                } catch (Exception ex) {
                    LOG.error("Failed to execute " + lTask.toString() + ", retry:" + retries, ex);
                    if (retries > 0) {
                        retries--;
                    } else {
                        LOG.error("Error executing task: ", ex);
                        LOG.error("Stopping LocalParWorker.");
                        break;
                    }
                }
            }
        }
    } catch (Exception ex) {
        LOG.error("Unexpected error during task execution: ", ex);
    }
}

private void cleanup(int pool) {
    if (OptimizerUtils.isSparkExecutionMode() && pool != -1) {
        SparkExecutionContext sec = (SparkExecutionContext) _ec;
        sec.cleanupThreadLocalSchedulerPool(pool);
    }
}

