/**
 * Constructs a {@link com.datatorrent.stram.util.StablePriorityQueue} class by absorbing all objects from a {@link java.util.Collection} object<p>
 * <br>
 * @param c a {@link java.util.Collection} object
 * <br>
 */
public StablePriorityQueue(Collection<? extends E> c) {
    this(c.size(), null);
    for (E e : c) {
        queue.add(new StableWrapper<>(e, counter++));
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

