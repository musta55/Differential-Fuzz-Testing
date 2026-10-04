@Override
public void threadFinished() {
    if (this.currentLock != null && this.currentLock.isHeldByCurrentThread()) {
        logWarning("Lock '{}' not released in: {}, releasing in threadFinished", getLockName(), getName());
        this.currentLock.unlock();
    }
    this.currentLock = null;
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

