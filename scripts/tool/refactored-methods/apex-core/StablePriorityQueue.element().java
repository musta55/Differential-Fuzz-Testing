@Override
public E element() throws NoSuchElementException {
    try {
        return queue.element().object;
    } catch (NoSuchElementException nsee) {
        resetCounter();
        throw nsee;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

