/**
 * Constructs a {@link com.datatorrent.stram.util.StablePriorityQueue} class with provided capacity<p>
 * <br>
 *
 * @param initialCapacity Size of the queue to be set up
 * @param comparator      {@link java.util.Comparator} object for comparison
 *                        <br>
 */
public StablePriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
    queue = new PriorityQueue<>(initialCapacity, comparator == null ? new StableWrapper.NaturalComparator<E>() : new StableWrapper.ProvidedComparator<>(comparator));
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

