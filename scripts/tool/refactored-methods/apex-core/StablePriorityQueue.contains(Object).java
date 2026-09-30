@Override
public boolean contains(Object o) {
    for (StableWrapper<E> e : queue) {
        if (e.object.equals(o)) {
            return true;
        }
    }
    return false;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

