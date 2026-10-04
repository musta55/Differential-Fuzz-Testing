/**
 * @see org.apache.jmeter.control.Controller#next()
 */
@Override
public Sampler next() {
    if (StringUtils.isEmpty(getLockName())) {
        logWarning("Empty lock name in Critical Section Controller: {}", getName());
        return super.next();
    }
    if (isFirst()) {
        // Take the lock for first child element
        long startTime = System.currentTimeMillis();
        if (this.currentLock == null) {
            this.currentLock = getOrCreateLock();
        }
        this.currentLock.lock();
        long endTime = System.currentTimeMillis();
        logDebug("Thread ('{}') acquired lock: '{}' in Critical Section Controller {}  in: {} ms", Thread.currentThread(), getLockName(), getName(), endTime - startTime);
    }
    return super.next();
}
// ---- helper method(s) introduced by the refactoring ----
private static void logDebug(String message, Object... args) {
    if (log.isDebugEnabled()) {
        log.debug(message, args);
    }
}

private static void logWarning(String message, Object... args) {
    if (log.isWarnEnabled()) {
        log.warn(message, args);
    }
}

