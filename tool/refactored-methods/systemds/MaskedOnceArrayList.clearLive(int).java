@SuppressWarnings("rawtypes")
public void clearLive(int i) {
    checkIndex(i);
    int partitionIndex = partitionIndex(i);
    MaskedOnceArray[] partitions = getPartitions();
    if (partitionIndex < partitions.length) {
        MaskedOnceArray partition = (MaskedOnceArray) PARTITION.getAcquire(partitions, partitionIndex);
        if (partition != null)
            partition.clearLive(offsetInPartition(i));
    }
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("rawtypes")
private MaskedOnceArray[] getPartitions() {
    return (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
}

