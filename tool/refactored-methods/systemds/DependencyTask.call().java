@Override
public E call() throws Exception {
    LOG.debug("Executing Task: " + this);
    long startTime = System.nanoTime();
    E result = executeTask();
    logExecutionTime(startTime);
    submitDependentTasks();
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private E executeTask() throws Exception {
    return _task.call();
}

private void logExecutionTime(long startTime) {
    long duration = System.nanoTime() - startTime;
    LOG.debug("Finished Task: " + this + " in: " + String.format("%.3f", duration * 1e-9) + "sec.");
}

private void submitDependentTasks() {
    _dependantTasks.forEach(this::submitIfReady);
}

private void submitIfReady(DependencyTask<?> task) {
    if (task.decrease()) {
        if (_pool == null) {
            throw new DMLRuntimeException("ExecutorService was not set for DependencyTask");
        }
        task._future.complete(_pool.submit(task));
    }
}

