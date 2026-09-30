/**
 * Constructs a {@link com.datatorrent.stram.util.StablePriorityQueue} class with provided capacity<p>
 * <br>
 *
 * @param initialCapacity Size of the queue to be set up
 * @param comparator      {@link java.util.Comparator} object for comparison
 *                        <br>
 */
public StablePriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
    queue = new PriorityQueue<>(initialCapacity, new StableWrapper.ProvidedComparator<>(comparator));
}