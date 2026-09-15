@Override
public boolean addAll(Collection<? extends E> c) {
    if (c == null || c.isEmpty()) {
        return false;
    }
    boolean modified = false;
    for (E e : c) {
        if (add(e)) {
            modified = true;
        }
    }
    return modified;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

