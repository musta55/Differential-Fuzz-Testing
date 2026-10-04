int append(long streamId, long tileId, Object value, long size) {
    ensureCapacity(count + 1);
    int slot = count++;
    setSlotValues(slot, streamId, tileId, value, size);
    return slot;
}
// ---- helper method(s) introduced by the refactoring ----
private void setSlotValues(int slot, long streamId, long tileId, Object value, long size) {
    streamIds[slot] = streamId;
    tileIds[slot] = tileId;
    values[slot] = value;
    sizes[slot] = size;
    refCounts[slot] = 1;
    bytes += size;
    activePins++;
}

private int calculateNewLength(int minSize) {
    int len = values.length;
    while (minSize > len) len <<= 1;
    return len;
}

private void resizeArrays(int newLength) {
    streamIds = Arrays.copyOf(streamIds, newLength);
    tileIds = Arrays.copyOf(tileIds, newLength);
    values = Arrays.copyOf(values, newLength);
    sizes = Arrays.copyOf(sizes, newLength);
    refCounts = Arrays.copyOf(refCounts, newLength);
}

