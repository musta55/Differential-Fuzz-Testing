public Object invoke(final InvocationContext context) throws Exception {
    if (timeout > 0) {
        try {
            if (!semaphore.tryAcquire(weight, timeout, TimeUnit.MILLISECONDS)) {
                throw new IllegalStateException("Can't acquire " + weight + " permits for " + context.getMethod() + " in " + timeout + "ms");
            }
        } catch (final InterruptedException e) {
            return onInterruption(e);
        }
    } else {
        try {
            semaphore.acquire(weight);
        } catch (final InterruptedException e) {
            return onInterruption(e);
        }
    }
    try {
        return context.proceed();
    } finally {
        semaphore.release(weight);
    }
}