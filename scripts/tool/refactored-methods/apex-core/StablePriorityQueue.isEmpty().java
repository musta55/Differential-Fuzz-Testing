@Override
public boolean isEmpty() {
    boolean isEmpty = queue.isEmpty();
    if (isEmpty) {
        resetCounter();
    }
    return isEmpty;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

