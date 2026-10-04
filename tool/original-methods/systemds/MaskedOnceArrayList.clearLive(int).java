@SuppressWarnings("rawtypes")
public void clearLive(int i) {
    checkIndex(i);
    int partition = partitionIndex(i);
    MaskedOnceArray[] partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    if (partition < partitions.length) {
        MaskedOnceArray p = (MaskedOnceArray) PARTITION.getAcquire(partitions, partition);
        if (p != null)
            p.clearLive(offsetInPartition(i));
    }
}