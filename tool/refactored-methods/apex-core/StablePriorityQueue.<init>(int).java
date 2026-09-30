/**
 * Constructs a {@link com.datatorrent.stram.util.StablePriorityQueue} class<p>
 * <br>
 * @param initialCapacity The size of the queue to be set up
 * <br>
 */
public StablePriorityQueue(int initialCapacity) {
    this(initialCapacity, null);
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

