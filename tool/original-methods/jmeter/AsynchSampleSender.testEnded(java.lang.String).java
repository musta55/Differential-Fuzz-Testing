@Override
public void testEnded(String host) {
    log.debug("Test Ended on {}", host);
    try {
        listener.testEnded(host);
        queue.put(FINAL_EVENT);
    } catch (Exception ex) {
        log.warn("testEnded(host)", ex);
    }
    if (queueWaits > 0) {
        log.info("QueueWaits: {}; QueueWaitTime: {} (nanoseconds)", queueWaits, queueWaitTime);
    }
}