@Override
public void threadFinished() {
    if (this.currentLock != null && this.currentLock.isHeldByCurrentThread()) {
        if (log.isWarnEnabled()) {
            log.warn("Lock '{}' not released in: {}, releasing in threadFinished", getLockName(), getName());
        }
        this.currentLock.unlock();
    }
    this.currentLock = null;
}