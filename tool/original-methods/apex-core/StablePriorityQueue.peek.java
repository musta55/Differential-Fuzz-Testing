@Override
public E peek() {
    StableWrapper<E> sw = queue.peek();
    if (sw == null) {
        return null;
    }
    return sw.object;
}