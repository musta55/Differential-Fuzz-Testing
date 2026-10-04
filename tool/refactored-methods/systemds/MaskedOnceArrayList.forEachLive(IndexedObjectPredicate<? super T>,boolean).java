@SuppressWarnings({ "rawtypes", "unchecked" })
public void forEachLive(IndexedObjectPredicate<? super T> action, boolean reversed) {
    MaskedOnceArray[] partitions = getPartitions();
    int start = reversed ? partitions.length - 1 : 0;
    int end = reversed ? -1 : partitions.length;
    int increment = reversed ? -1 : 1;
    for (int i = start; i != end; i += increment) {
        MaskedOnceArray partition = (MaskedOnceArray) PARTITION.getAcquire(partitions, i);
        if (partition != null)
            partition.forEachLive(action, reversed, i * _partitionSize);
    }
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("rawtypes")
private MaskedOnceArray[] getPartitions() {
    return (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
}

