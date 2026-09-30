@Override
public E remove() throws NoSuchElementException {
    try {
        return queue.remove().object;
    } catch (NoSuchElementException nsee) {
        resetCounter();
        throw nsee;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

