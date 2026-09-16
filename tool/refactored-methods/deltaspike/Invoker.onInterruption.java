private static void onInterruption(final InterruptedException e) {
    Thread.interrupted();
    throw ExceptionUtils.throwAsRuntimeException(e);
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

