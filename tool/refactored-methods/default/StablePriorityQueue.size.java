@Override
public int size() {
    int size = queue.size();
    if (size == 0) {
        resetCounter();
    }
    return size;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

