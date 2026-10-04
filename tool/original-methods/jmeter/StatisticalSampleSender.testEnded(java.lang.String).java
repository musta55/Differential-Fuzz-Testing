/**
 * Checks if any sample events are still present in the sampleStore and
 * sends them to the listener. Informs the listener that the test ended.
 *
 * @param host the hostname that the test has ended on.
 */
@Override
public void testEnded(String host) {
    log.info("Test Ended on {}", host);
    try {
        if (!sampleStore.isEmpty()) {
            sendBatch();
        }
        listener.testEnded(host);
    } catch (RemoteException err) {
        log.warn("testEnded(hostname)", err);
    }
}