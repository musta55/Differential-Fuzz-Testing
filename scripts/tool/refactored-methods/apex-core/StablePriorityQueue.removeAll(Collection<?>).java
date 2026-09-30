@Override
public boolean removeAll(Collection<?> c) {
    if (c == null) {
        return false;
    }
    if (c == this) {
        if (!isEmpty()) {
            clear();
            return true;
        }
        return false;
    }
    boolean modified = false;
    for (Object o : c) {
        if (remove(o)) {
            modified = true;
        }
    }
    if (modified && isEmpty()) {
        resetCounter();
    }
    return modified;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

