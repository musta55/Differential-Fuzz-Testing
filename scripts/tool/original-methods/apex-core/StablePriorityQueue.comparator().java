@SuppressWarnings({ "unchecked", "rawtypes" })
public Comparator<? super E> comparator() {
    Comparator<? super StableWrapper<E>> comparator = queue.comparator();
    if (comparator instanceof StableWrapper.ProvidedComparator) {
        return ((StableWrapper.ProvidedComparator) comparator).comparator;
    }
    return null;
}