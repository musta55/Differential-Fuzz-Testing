@SuppressWarnings("rawtypes")
public boolean clear(int i) {
    checkIndex(i);
    int partition = partitionIndex(i);
    MaskedOnceArray[] partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    if (partition < partitions.length)
        return clear(partitions, partition, offsetInPartition(i));
    return false;
}