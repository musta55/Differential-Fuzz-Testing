@Override
public void put(Object tuple) {
    try {
        reservoir.put(tuple);
        handleTuple(tuple);
    } catch (InterruptedException ie) {
        handleInterruptedException(ie);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleTuple(Object tuple) {
    if (!(tuple instanceof Tuple)) {
        count++;
    }
}

private void handleInterruptedException(InterruptedException ie) {
    logger.debug("Interrupted", ie);
    throw new RuntimeException(ie);
}

private int retrieveCount() {
    return count;
}

private void resetCountIfRequired(boolean reset) {
    if (reset) {
        count = 0;
    }
}

