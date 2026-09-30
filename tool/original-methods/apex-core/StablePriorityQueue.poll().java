@Override
public E poll() {
    StableWrapper<E> sw = queue.poll();
    if (sw == null) {
        return null;
    }
    return sw.object;
}