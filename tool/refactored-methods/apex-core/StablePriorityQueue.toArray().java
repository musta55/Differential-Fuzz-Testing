@SuppressWarnings("unchecked")
@Override
public Object[] toArray() {
    Object[] array = queue.toArray();
    for (int i = 0; i < array.length; i++) {
        array[i] = ((StableWrapper<E>) array[i]).object;
    }
    return array;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

