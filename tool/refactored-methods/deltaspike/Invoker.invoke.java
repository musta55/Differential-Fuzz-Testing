public Object invoke(final InvocationContext context) throws Exception {
    if (timeout > 0) {
        if (!tryAcquireWithTimeout()) {
            throw new IllegalStateException("Can't acquire " + weight + " permits for " + context.getMethod() + " in " + timeout + "ms");
        }
    } else {
        tryAcquireWithoutTimeout();
    }
    try {
        return context.proceed();
    } finally {
        semaphore.release(weight);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean tryAcquireWithTimeout() {
    try {
        return semaphore.tryAcquire(weight, timeout, TimeUnit.MILLISECONDS);
    } catch (final InterruptedException e) {
        onInterruption(e);
        return false;
    }
}

private void tryAcquireWithoutTimeout() {
    try {
        semaphore.acquire(weight);
    } catch (final InterruptedException e) {
        onInterruption(e);
    }
}

