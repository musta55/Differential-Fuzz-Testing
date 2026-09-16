public MonitorResultEvent(Map<String, AtomicInteger> methodInvocations, Map<String, AtomicInteger> classInvocations, Map<String, AtomicLong> methodDurations) {
    this.methodInvocations = methodInvocations;
    this.classInvocations = classInvocations;
    this.methodDurations = methodDurations;
}