@Override
public E element() throws NoSuchElementException {
    try {
        return queue.element().object;
    } catch (NoSuchElementException nsee) {
        counter = 0;
        throw nsee;
    }
}