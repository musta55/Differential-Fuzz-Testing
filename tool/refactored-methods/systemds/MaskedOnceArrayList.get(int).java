@SuppressWarnings({ "unchecked", "rawtypes" })
public T get(int i) {
    checkIndex(i);
    int partitionIndex = partitionIndex(i);
    MaskedOnceArray[] partitions = getPartitions();
    if (partitionIndex >= partitions.length)
        return null;
    MaskedOnceArray partition = (MaskedOnceArray) PARTITION.getAcquire(partitions, partitionIndex);
    return partition == null ? null : (T) partition.get(offsetInPartition(i));
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("rawtypes")
private MaskedOnceArray[] getPartitions() {
    return (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
}

