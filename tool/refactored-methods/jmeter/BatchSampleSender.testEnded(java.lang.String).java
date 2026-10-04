/**
 * Checks if any sample events are still present in the sampleStore and
 * sends them to the listener. Informs the listener of the testended.
 *
 * @param host
 *            the host that the test has ended on.
 */
@Override
public void testEnded(String host) {
    log.info("Test Ended on {}", host);
    try {
        sendStoredSamples();
        listener.testEnded(host);
    } catch (RemoteException err) {
        log.error("testEnded(host)", err);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldSendNow() {
    int sampleCount = sampleStore.size();
    boolean sendNow = false;
    if (numSamplesThreshold != -1 && sampleCount >= numSamplesThreshold) {
        sendNow = true;
    }
    if (!sendNow && timeThresholdMs != -1) {
        long now = System.currentTimeMillis();
        if (batchSendTime == -1) {
            batchSendTime = now + timeThresholdMs;
        }
        if (batchSendTime < now && sampleCount > 0) {
            sendNow = true;
        }
    }
    return sendNow;
}

private List<SampleEvent> cloneAndClearSampleStore() {
    // OK because sampleStore is of type ArrayList<SampleEvent>
    @SuppressWarnings("unchecked")
    final ArrayList<SampleEvent> clone = (ArrayList<SampleEvent>) ((ArrayList<SampleEvent>) sampleStore).clone();
    sampleStore.clear();
    if (timeThresholdMs != -1) {
        batchSendTime = System.currentTimeMillis() + timeThresholdMs;
    }
    return clone;
}

private void sendStoredSamples() {
    if (!sampleStore.isEmpty()) {
        try {
            listener.processBatch(sampleStore);
            sampleStore.clear();
        } catch (RemoteException err) {
            log.error("testEnded(host)", err);
        }
    }
}

private void sendSamples(List<SampleEvent> samples) {
    try {
        log.debug("Firing sample");
        listener.processBatch(samples);
        samples.clear();
    } catch (RemoteException err) {
        log.error("sampleOccurred", err);
    }
}

