@SuppressWarnings("unchecked")
@Override
public <T> T[] toArray(T[] a) {
    T[] finalArray;
    queue.toArray(a);
    final int length = queue.size();
    if (a.length < length) {
        finalArray = (T[]) Array.newInstance(a.getClass().getComponentType(), length);
    } else {
        finalArray = a;
    }
    Iterator<StableWrapper<E>> iterator = queue.iterator();
    for (int i = 0; i < length; i++) {
        if (iterator.hasNext()) {
            finalArray[i] = (T) iterator.next().object;
        } else {
            if (finalArray != a) {
                finalArray = Arrays.copyOf(finalArray, i);
            } else {
                finalArray[i] = null;
            }
        }
    }
    return finalArray;
}