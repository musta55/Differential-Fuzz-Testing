@Override
public boolean contains(int i) {
    if (i < cols[0] || i > cols[cols.length - 1])
        return false;
    int id = Arrays.binarySearch(cols, 0, cols.length, i);
    return id >= 0;
}