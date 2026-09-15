@Override
public E poll() {
    StableWrapper<E> sw = queue.poll();
    return sw == null ? null : sw.object;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

