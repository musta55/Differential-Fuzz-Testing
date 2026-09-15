@Override
public boolean contains(Object o) {
    for (StableWrapper<E> e : queue) {
        if (e.object == o) {
            return true;
        }
    }
    return false;
}