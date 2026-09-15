@Override
public boolean isEmpty() {
    boolean isEmpty = queue.isEmpty();
    if (isEmpty) {
        counter = 0;
    }
    return isEmpty;
}