@Override
public E remove() throws NoSuchElementException {
    try {
        return queue.remove().object;
    } catch (NoSuchElementException nsee) {
        counter = 0;
        throw nsee;
    }
}