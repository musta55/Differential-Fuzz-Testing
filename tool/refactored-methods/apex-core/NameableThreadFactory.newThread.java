@Override
public Thread newThread(Runnable r) {
    Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
    setDaemonStatus(t);
    setPriority(t);
    return t;
}
// ---- helper method(s) introduced by the refactoring ----
private void setDaemonStatus(Thread t) {
    if (t.isDaemon() != isDaemon) {
        t.setDaemon(isDaemon);
    }
}

private void setPriority(Thread t) {
    if (t.getPriority() != Thread.NORM_PRIORITY) {
        t.setPriority(Thread.NORM_PRIORITY);
    }
}

