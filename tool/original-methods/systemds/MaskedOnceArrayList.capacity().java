@SuppressWarnings("rawtypes")
public int capacity() {
    MaskedOnceArray[] partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    return partitions.length * _partitionSize;
}