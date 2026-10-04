@SuppressWarnings("rawtypes")
private MaskedOnceArray[] ensurePartitionCapacity(int partitionIndex) {
    MaskedOnceArray[] partitions = getPartitions();
    while (partitionIndex >= partitions.length) {
        MaskedOnceArray[] bigger = growPartitions(partitions, partitionIndex + 1);
        if (PARTITIONS.compareAndSet(this, partitions, bigger))
            partitions = bigger;
        else
            partitions = getPartitions();
    }
    return partitions;
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("rawtypes")
private MaskedOnceArray[] getPartitions() {
    return (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
}

