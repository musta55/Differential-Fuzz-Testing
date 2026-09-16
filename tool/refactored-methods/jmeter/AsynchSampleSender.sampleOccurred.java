@Override
public void sampleOccurred(SampleEvent e) {
    try {
        if (!queue.offer(e)) {
            // we failed to add the element first time
            handleQueueWait(e);
        }
    } catch (Exception err) {
        log.error("sampleOccurred; failed to queue the sample", err);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleQueueWait(SampleEvent e) throws InterruptedException {
    queueWaits++;
    long startTime = System.nanoTime();
    queue.put(e);
    long endTime = System.nanoTime();
    queueWaitTime += endTime - startTime;
}

private void logQueueStats() {
    if (queueWaits > 0) {
        log.info("QueueWaits: {}; QueueWaitTime: {} (nanoseconds)", queueWaits, queueWaitTime);
    }
}

