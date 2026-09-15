@SuppressWarnings("unchecked")
@Override
public <T> T[] toArray(T[] a) {
    T[] finalArray = a.length >= queue.size() ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), queue.size());
    int index = 0;
    for (StableWrapper<E> swe : queue) {
        finalArray[index++] = (T) swe.object;
    }
    if (index < finalArray.length) {
        finalArray[index] = null;
    }
    return finalArray;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

