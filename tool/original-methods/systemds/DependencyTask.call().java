@Override
public E call() throws Exception {
    LOG.debug("Executing Task: " + this);
    long t0 = System.nanoTime();
    E ret = _task.call();
    LOG.debug("Finished Task: " + this + " in: " + (String.format("%.3f", (System.nanoTime() - t0) * 1e-9)) + "sec.");
    _dependantTasks.forEach(t -> {
        if (t.decrease()) {
            if (_pool == null)
                throw new DMLRuntimeException("ExecutorService was not set for DependencyTask");
            t._future.complete(_pool.submit(t));
        }
    });
    return ret;
}