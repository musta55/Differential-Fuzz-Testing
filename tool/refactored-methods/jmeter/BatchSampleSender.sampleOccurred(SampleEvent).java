/**
 * Stores sample events until either a time or sample threshold is
 * breached. Both thresholds are reset if one fires. If only one threshold
 * is set it becomes the only value checked against. When a threshold is
 * breached the list of sample events is sent to a listener where the event
 * are fired locally.
 *
 * @param e
 *            a Sample Event
 */
@Override
public void sampleOccurred(SampleEvent e) {
    List<SampleEvent> clonedStore = null;
    synchronized (sampleStore) {
        sampleStore.add(e);
        if (shouldSendNow()) {
            clonedStore = cloneAndClearSampleStore();
        }
    }
    // synchronized(sampleStore)
    if (clonedStore != null) {
        sendSamples(clonedStore);
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

