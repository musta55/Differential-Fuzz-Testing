public static void setContext(TaskContext context) {
    if (context == null) {
        throw new IllegalArgumentException("Context cannot be null");
    }
    if (CTX.get() != null) {
        throw new IllegalStateException();
    }
    CTX.set(context);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeDeferredIfNecessary() {
    if (_deferred == null) {
        _deferred = new ArrayDeque<>();
    }
}

private void runDeferredTasks() {
    Runnable deferred;
    while ((deferred = _deferred.poll()) != null) {
        deferred.run();
    }
}

