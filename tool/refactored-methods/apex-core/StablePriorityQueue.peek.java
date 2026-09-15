@Override
public E peek() {
    StableWrapper<E> sw = queue.peek();
    return sw == null ? null : sw.object;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

