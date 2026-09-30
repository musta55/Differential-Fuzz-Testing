@SuppressWarnings("unchecked")
public StablePriorityQueue(StablePriorityQueue<? extends E> c) {
    this(c.size(), (Comparator<? super E>) c.comparator());
}
// ---- helper method(s) introduced by the refactoring ----
private void resetCounter() {
    counter = 0;
}

