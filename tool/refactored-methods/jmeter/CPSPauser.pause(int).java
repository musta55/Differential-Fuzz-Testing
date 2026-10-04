/**
 * Pause for an appropriate time according to the number of bytes being transferred.
 *
 * @param bytes number of bytes being transferred
 */
public void pause(int bytes) {
    long sleepMS = calculateSleepMS(bytes);
    int sleepNS = calculateSleepNS(bytes);
    try {
        if (sleepMS > 0 || sleepNS > 0) {
            Thread.sleep(sleepMS, sleepNS);
        }
    } catch (InterruptedException ignored) {
        // NOOP
        Thread.currentThread().interrupt();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private long calculateSleepMS(int bytes) {
    return (bytes * MS_PER_SEC) / charactersPerSecond;
}

private int calculateSleepNS(int bytes) {
    return (int) ((calculateSleepMS(bytes) % NS_PER_MS) * NS_PER_MS);
}

