@Override
public boolean hasNext() {
    if (sparse) {
        if (sparseIterator == null)
            hasValue = false;
        else
            hasValue = sparseIterator.hasNext();
    } else {
        if (denseArray == null)
            hasValue = false;
        else {
            while (nextInDenseArray < denseArraySize && denseArray[nextInDenseArray] == 0) nextInDenseArray++;
            hasValue = (nextInDenseArray < denseArraySize);
        }
    }
    return hasValue;
}