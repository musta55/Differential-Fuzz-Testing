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
        // Locate the statistical sample collector
        String key = StatisticalSampleResult.getKey(e, keyOnThreadName);
        StatisticalSampleResult statResult = sampleTable.get(key);
        if (statResult == null) {
            statResult = new StatisticalSampleResult(e.getResult());
            // store the new statistical result collector
            sampleTable.put(key, statResult);
            // add a new wrapper sampleevent
            sampleStore.add(new SampleEvent(statResult, e.getThreadGroup()));
        }
        statResult.add(e.getResult());
        sampleCount++;
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
            if (batchSendTime < now) {
                sendNow = true;
            }
        }
        if (sendNow) {
            try {
                log.debug("Firing sample");
                sendBatch();
                if (timeThresholdMs != -1) {
                    this.batchSendTime = now + timeThresholdMs;
                }
            } catch (RemoteException err) {
                log.warn("sampleOccurred", err);
            }
        }
    }
    // synchronized(sampleStore)
}