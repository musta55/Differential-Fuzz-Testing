@Override
public boolean addAll(Collection<? extends E> c) {
    if (c == null) {
        return queue.addAll(null);
    }
    if (c == this) {
        return queue.addAll(queue);
    }
    boolean modified = false;
    for (E e : c) {
        if (add(e)) {
            modified = true;
        }
    }
    return modified;
}