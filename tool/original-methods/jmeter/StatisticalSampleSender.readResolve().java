/**
 * Processed by the RMI server code; acts as testStarted().
 * @return this
 * @throws ObjectStreamException never
 */
private Object readResolve() throws ObjectStreamException {
    if (isClientConfigured()) {
        numSamplesThreshold = clientConfiguredNumSamplesThreshold;
        timeThresholdMs = clientConfiguredTimeThresholdMs;
        keyOnThreadName = clientConfiguredKeyOnThreadName;
    } else {
        numSamplesThreshold = NUM_SAMPLES_THRESHOLD;
        timeThresholdMs = TIME_THRESHOLD_MS;
        keyOnThreadName = KEY_ON_THREADNAME;
    }
    if (log.isInfoEnabled()) {
        log.info("Using StatisticalSampleSender for this run. {} config: Thresholds: num={}, time={}. Key uses ThreadName: {}", isClientConfigured() ? "Client" : "Server", numSamplesThreshold, timeThresholdMs, keyOnThreadName);
    }
    return this;
}