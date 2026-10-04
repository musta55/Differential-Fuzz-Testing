/**
 * Stores sample events until either a time or sample threshold is
 * breached. Both thresholds are reset if one fires. If only one threshold
 * is set it becomes the only value checked against. When a threshold is
 * breached the list of sample events is sent to a listener where the event
 * are fired locally.
 *
 * @param e a Sample Event
 */
@Override
public void sampleOccurred(SampleEvent e) {
    synchronized (sampleStore) {
        updateSampleTableWithEvent(e);
        incrementSampleCount();
        if (shouldSendBatch()) {
            try {
                log.debug("Firing sample");
                sendBatch();
                resetBatchSendTimeIfApplicable();
            } catch (RemoteException err) {
                log.warn("sampleOccurred", err);
            }
        }
    }
    // synchronized(sampleStore)
}
// ---- helper method(s) introduced by the refactoring ----
private void updateSampleTableWithEvent(SampleEvent e) {
    String key = StatisticalSampleResult.getKey(e, keyOnThreadName);
    StatisticalSampleResult statResult = sampleTable.get(key);
    if (statResult == null) {
        statResult = new StatisticalSampleResult(e.getResult());
        sampleTable.put(key, statResult);
        sampleStore.add(new SampleEvent(statResult, e.getThreadGroup()));
    }
    statResult.add(e.getResult());
}

private void incrementSampleCount() {
    sampleCount++;
}

private boolean shouldSendBatch() {
    boolean sendNow = false;
    if (numSamplesThreshold != -1 && sampleCount >= numSamplesThreshold) {
        sendNow = true;
    }
    if (timeThresholdMs != -1) {
        long now = System.currentTimeMillis();
        if (batchSendTime == -1) {
            batchSendTime = now + timeThresholdMs;
        }
        if (batchSendTime < now) {
            sendNow = true;
        }
    }
    return sendNow;
}

private void resetBatchSendTimeIfApplicable() {
    if (timeThresholdMs != -1) {
        batchSendTime = System.currentTimeMillis() + timeThresholdMs;
    }
}

private void clearSampleStoreAndTable() {
    sampleStore.clear();
    sampleTable.clear();
    sampleCount = 0;
}

private void sendStoredSamplesIfAny() throws RemoteException {
    if (!sampleStore.isEmpty()) {
        sendBatch();
    }
}

private void configureThresholdsAndKeyUsage() {
    if (isClientConfigured()) {
        numSamplesThreshold = clientConfiguredNumSamplesThreshold;
        timeThresholdMs = clientConfiguredTimeThresholdMs;
        keyOnThreadName = clientConfiguredKeyOnThreadName;
    } else {
        numSamplesThreshold = NUM_SAMPLES_THRESHOLD;
        timeThresholdMs = TIME_THRESHOLD_MS;
        keyOnThreadName = KEY_ON_THREADNAME;
    }
}

private void logConfigurationDetails() {
    if (log.isInfoEnabled()) {
        log.info("Using StatisticalSampleSender for this run. {} config: Thresholds: num={}, time={}. Key uses ThreadName: {}", isClientConfigured() ? "Client" : "Server", numSamplesThreshold, timeThresholdMs, keyOnThreadName);
    }
}

