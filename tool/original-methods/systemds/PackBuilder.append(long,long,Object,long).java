int append(long streamId, long tileId, Object value, long size) {
    ensureCapacity(count + 1);
    int slot = count++;
    streamIds[slot] = streamId;
    tileIds[slot] = tileId;
    values[slot] = value;
    sizes[slot] = size;
    refCounts[slot] = 1;
    bytes += size;
    activePins++;
    return slot;
}