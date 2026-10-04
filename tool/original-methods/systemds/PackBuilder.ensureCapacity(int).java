private void ensureCapacity(int minSize) {
    if (minSize <= values.length)
        return;
    int len = values.length;
    while (minSize > len) len <<= 1;
    streamIds = Arrays.copyOf(streamIds, len);
    tileIds = Arrays.copyOf(tileIds, len);
    values = Arrays.copyOf(values, len);
    sizes = Arrays.copyOf(sizes, len);
    refCounts = Arrays.copyOf(refCounts, len);
}