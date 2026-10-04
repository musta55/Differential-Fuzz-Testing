@SuppressWarnings("rawtypes")
public int capacity() {
    MaskedOnceArray[] partitions = getPartitions();
    return partitions.length * _partitionSize;
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("rawtypes")
private MaskedOnceArray[] getPartitions() {
    return (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
}

