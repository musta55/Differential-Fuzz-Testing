@SuppressWarnings("unchecked")
@Override
public Object[] toArray() {
    Object[] array = queue.toArray();
    for (int i = array.length; i-- > 0; ) {
        array[i] = ((StableWrapper<E>) array[i]).object;
    }
    return array;
}