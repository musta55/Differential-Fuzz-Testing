@Override
public boolean remove(Object o) {
    for (StableWrapper<E> e : queue) {
        if (e.object.equals(o)) {
            if (queue.size() == 1) {
                resetCounter();
            }
            return queue.remove(e);
        }
    }
    return false;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

