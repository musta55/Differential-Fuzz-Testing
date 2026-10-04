public static boolean runDeferred() {
    TaskContext ctx = CTX.get();
    if (ctx == null || ctx._deferred == null || ctx._deferred.isEmpty()) {
        return false;
    }
    ctx.runDeferredTasks();
    return true;
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

