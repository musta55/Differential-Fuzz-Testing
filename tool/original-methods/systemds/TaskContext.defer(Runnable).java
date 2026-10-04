public static void defer(Runnable deferred) {
    TaskContext ctx = CTX.get();
    if (ctx == null) {
        deferred.run();
        return;
    }
    if (ctx._deferred == null)
        ctx._deferred = new ArrayDeque<>();
    ctx._deferred.add(deferred);
}