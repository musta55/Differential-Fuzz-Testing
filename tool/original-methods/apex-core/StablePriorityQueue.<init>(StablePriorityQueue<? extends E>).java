@SuppressWarnings("unchecked")
public StablePriorityQueue(StablePriorityQueue<? extends E> c) {
    queue = new PriorityQueue<>(c.size(), (Comparator<? super StableWrapper<E>>) c.comparator());
}