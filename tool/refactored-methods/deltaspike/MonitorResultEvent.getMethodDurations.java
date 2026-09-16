/**
 * @return Map with duration for all method invocations
 * key = fully qualified method name (includes class)
 * value = AtomicLong with duration nanos
 */
public Map<String, AtomicLong> getMethodDurations() {
    return new HashMap<>(methodDurations);
}