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
        final int sampleCount = sampleStore.size();
        boolean sendNow = false;
        if (numSamplesThreshold != -1) {
            if (sampleCount >= numSamplesThreshold) {
                sendNow = true;
            }
        }
        long now = 0;
        if (timeThresholdMs != -1) {
            now = System.currentTimeMillis();
            // Checking for and creating initial timestamp to check against
            if (batchSendTime == -1) {
                this.batchSendTime = now + timeThresholdMs;
            }
            if (batchSendTime < now && sampleCount > 0) {
                sendNow = true;
            }
        }
        if (sendNow) {
            // OK because sampleStore is of type ArrayList<SampleEvent>
            @SuppressWarnings("unchecked")
            final ArrayList<SampleEvent> clone = (ArrayList<SampleEvent>) ((ArrayList<SampleEvent>) sampleStore).clone();
            clonedStore = clone;
            sampleStore.clear();
            if (timeThresholdMs != -1) {
                this.batchSendTime = now + timeThresholdMs;
            }
        }
    }
    // synchronized(sampleStore)
    if (clonedStore != null) {
        try {
            log.debug("Firing sample");
            listener.processBatch(clonedStore);
            clonedStore.clear();
        } catch (RemoteException err) {
            log.error("sampleOccurred", err);
        }
    }
}