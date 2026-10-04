@SuppressWarnings({ "unchecked", "rawtypes" })
public T get(int i) {
    checkIndex(i);
    int partition = partitionIndex(i);
    MaskedOnceArray[] partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    if (partition >= partitions.length)
        return null;
    MaskedOnceArray p = (MaskedOnceArray) PARTITION.getAcquire(partitions, partition);
    return p == null ? null : (T) p.get(offsetInPartition(i));
}