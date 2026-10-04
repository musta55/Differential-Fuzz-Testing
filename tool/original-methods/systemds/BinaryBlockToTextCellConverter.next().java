@Override
public Pair<NullWritable, Text> next() {
    if (!hasValue)
        return null;
    long i, j;
    double v;
    if (sparse) {
        if (sparseIterator == null)
            return null;
        IJV cell = sparseIterator.next();
        i = cell.getI() + startIndexes.getRowIndex();
        j = cell.getJ() + startIndexes.getColumnIndex();
        v = cell.getV();
    } else {
        if (denseArray == null)
            return null;
        i = startIndexes.getRowIndex() + nextInDenseArray / thisBlockWidth;
        j = startIndexes.getColumnIndex() + nextInDenseArray % thisBlockWidth;
        v = denseArray[nextInDenseArray];
        nextInDenseArray++;
    }
    value.set(i + " " + j + " " + v);
    return pair;
}