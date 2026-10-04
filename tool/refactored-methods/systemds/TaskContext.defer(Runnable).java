public static void defer(Runnable deferred) {
    if (deferred == null) {
        throw new IllegalArgumentException("Deferred task cannot be null");
    }
    TaskContext ctx = CTX.get();
    if (ctx == null) {
        deferred.run();
        return;
    }
    ctx.initializeDeferredIfNecessary();
    ctx._deferred.add(deferred);
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

