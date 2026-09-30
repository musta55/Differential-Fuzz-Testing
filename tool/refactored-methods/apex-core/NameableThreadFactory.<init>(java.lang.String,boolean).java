public NameableThreadFactory(String groupname, boolean isDaemon) {
    SecurityManager s = System.getSecurityManager();
    group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    namePrefix = groupname + "-" + poolNumber.getAndIncrement() + "-";
    this.isDaemon = isDaemon;
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

