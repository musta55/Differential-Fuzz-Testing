@SuppressWarnings({ "rawtypes", "unchecked" })
public void forEachLive(IndexedObjectPredicate<? super T> action, boolean reversed) {
    MaskedOnceArray[] partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    if (reversed) {
        for (int i = partitions.length - 1; i >= 0; i--) {
            MaskedOnceArray partition = (MaskedOnceArray) PARTITION.getAcquire(partitions, i);
            if (partition != null)
                partition.forEachLive(action, true, i * _partitionSize);
        }
    } else {
        for (int i = 0; i < partitions.length; i++) {
            MaskedOnceArray partition = (MaskedOnceArray) PARTITION.getAcquire(partitions, i);
            if (partition != null)
                partition.forEachLive(action, false, i * _partitionSize);
        }
    }
}