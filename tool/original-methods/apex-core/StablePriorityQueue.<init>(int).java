/**
 * Constructs a {@link com.datatorrent.stram.util.StablePriorityQueue} class<p>
 * <br>
 * @param initialCapacity The size of the queue to be set up
 * <br>
 */
public StablePriorityQueue(int initialCapacity) {
    queue = new PriorityQueue<>(initialCapacity, new StableWrapper.NaturalComparator<E>());
}