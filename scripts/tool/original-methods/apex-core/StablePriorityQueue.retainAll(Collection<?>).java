@Override
public boolean retainAll(Collection<?> c) {
    ArrayList<StableWrapper<E>> removeThese = new ArrayList<>();
    for (StableWrapper<E> swe : queue) {
        if (!c.contains(swe.object)) {
            removeThese.add(swe);
        }
    }
    if (removeThese.isEmpty()) {
        return false;
    }
    if (queue.size() == removeThese.size()) {
        counter = 0;
    }
    return queue.removeAll(removeThese);
}