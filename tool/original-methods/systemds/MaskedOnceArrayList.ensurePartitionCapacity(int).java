@SuppressWarnings("rawtypes")
private MaskedOnceArray[] ensurePartitionCapacity(int partitionIndex) {
    MaskedOnceArray[] partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    while (partitionIndex >= partitions.length) {
        MaskedOnceArray[] bigger = growPartitions(partitions, partitionIndex + 1);
        if (PARTITIONS.compareAndSet(this, partitions, bigger))
            partitions = bigger;
        else
            partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    }
    return partitions;
}