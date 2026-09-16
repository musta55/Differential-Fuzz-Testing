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
        if (!sampleStore.isEmpty()) {
            listener.processBatch(sampleStore);
            sampleStore.clear();
        }
        listener.testEnded(host);
    } catch (RemoteException err) {
        log.error("testEnded(host)", err);
    }
}