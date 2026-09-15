@Override
public boolean remove(Object o) {
    for (StableWrapper<E> e : queue) {
        if (e.object == o) {
            if (size() == 1) {
                counter = 0;
            }
            return queue.remove(e);
        }
    }
    return false;
}