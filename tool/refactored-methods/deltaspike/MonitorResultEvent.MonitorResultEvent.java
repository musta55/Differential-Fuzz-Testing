public MonitorResultEvent(Map<String, AtomicInteger> methodInvocations, Map<String, AtomicInteger> classInvocations, Map<String, AtomicLong> methodDurations) {
    this.methodInvocations = new HashMap<>(methodInvocations);
    this.classInvocations = new HashMap<>(classInvocations);
    this.methodDurations = new HashMap<>(methodDurations);
}