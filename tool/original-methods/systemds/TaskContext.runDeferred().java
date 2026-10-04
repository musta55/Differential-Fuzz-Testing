public static boolean runDeferred() {
    TaskContext ctx = CTX.get();
    if (ctx == null || ctx._deferred == null || ctx._deferred.isEmpty())
        return false;
    Runnable deferred;
    while ((deferred = ctx._deferred.poll()) != null) deferred.run();
    return true;
}