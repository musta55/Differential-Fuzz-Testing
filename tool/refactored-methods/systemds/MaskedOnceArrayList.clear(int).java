@SuppressWarnings("rawtypes")
public boolean clear(int i) {
    checkIndex(i);
    int partitionIndex = partitionIndex(i);
    MaskedOnceArray[] partitions = getPartitions();
    return partitionIndex < partitions.length && clear(partitions, partitionIndex, offsetInPartition(i));
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("rawtypes")
private MaskedOnceArray[] getPartitions() {
    return (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
}

