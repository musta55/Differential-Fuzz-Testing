@Override
public void clear() {
    queue.clear();
    resetCounter();
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

