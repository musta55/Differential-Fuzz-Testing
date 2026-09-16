@Override
public void testEnded(String host) {
    log.debug("Test Ended on {}", host);
    try {
        listener.testEnded(host);
        queue.put(FINAL_EVENT);
    } catch (Exception ex) {
        log.warn("testEnded(host)", ex);
    }
    logQueueStats();
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

