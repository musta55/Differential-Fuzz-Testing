@Override
public boolean retainAll(Collection<?> c) {
    if (c == null) {
        return false;
    }
    ArrayList<StableWrapper<E>> removeThese = new ArrayList<>();
    for (StableWrapper<E> swe : queue) {
        if (!c.contains(swe.object)) {
            removeThese.add(swe);
        }
    }
    if (removeThese.isEmpty()) {
        return false;
    }
    if (queue.size() == removeThese.size()) {
        resetCounter();
    }
    return queue.removeAll(removeThese);
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

