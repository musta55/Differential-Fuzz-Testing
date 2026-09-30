/**
 * Constructs a {@link com.datatorrent.stram.util.StablePriorityQueue} class by absorbing all objects from a {@link java.util.Collection} object<p>
 * <br>
 * @param c a {@link java.util.Collection} object
 * <br>
 */
public StablePriorityQueue(Collection<? extends E> c) {
    queue = new PriorityQueue<>(c.size(), new StableWrapper.NaturalComparator<E>());
    for (E e : c) {
        queue.add(new StableWrapper<>(e, counter++));
    }
}