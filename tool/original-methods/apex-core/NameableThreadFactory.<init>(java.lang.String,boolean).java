public NameableThreadFactory(String groupname, boolean isDaemon) {
    SecurityManager s = java.lang.System.getSecurityManager();
    group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    namePrefix = groupname + "-" + poolNumber.getAndIncrement() + "-";
    this.isDaemon = isDaemon;
}