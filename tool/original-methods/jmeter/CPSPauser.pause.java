/**
 * Pause for an appropriate time according to the number of bytes being transferred.
 *
 * @param bytes number of bytes being transferred
 */
public void pause(int bytes) {
    long sleepMS = (bytes * MS_PER_SEC) / charactersPerSecond;
    // NOSONAR Where is the boxing to Long
    int sleepNS = Long.valueOf(sleepMS % NS_PER_MS).intValue();
    try {
        if (sleepMS > 0 || sleepNS > 0) {
            Thread.sleep(sleepMS, sleepNS);
        }
    } catch (InterruptedException ignored) {
        // NOOP
        Thread.currentThread().interrupt();
    }
}