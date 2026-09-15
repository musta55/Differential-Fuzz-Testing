@SuppressWarnings({ "unchecked", "rawtypes" })
public Comparator<? super E> comparator() {
    Comparator<? super StableWrapper<E>> comparator = queue.comparator();
    return comparator instanceof StableWrapper.ProvidedComparator ? ((StableWrapper.ProvidedComparator) comparator).comparator : null;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

