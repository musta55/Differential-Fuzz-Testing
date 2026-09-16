/**
 * @return Map with Counter for all class invocations
 * key = fully qualified class name
 * value = AtomicInteger with invocation count value
 */
public Map<String, AtomicInteger> getClassInvocations() {
    return new HashMap<>(classInvocations);
}