/**
 * @return Map with Counters for all method invocations
 * key = fully qualified method name (includes class)
 * value = AtomicInteger with invocation count value
 */
public Map<String, AtomicInteger> getMethodInvocations() {
    return new HashMap<>(methodInvocations);
}