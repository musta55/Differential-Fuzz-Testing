@SuppressWarnings({ "rawtypes", "unchecked" })
public void forEachVisible(Consumer<? super T> action) {
    MaskedOnceArray[] partitions = getPartitions();
    for (int i = 0; i < partitions.length; i++) {
        MaskedOnceArray partition = (MaskedOnceArray) PARTITION.getAcquire(partitions, i);
        if (partition != null)
            partition.forEachVisible(action);
    }
}
// ---- helper method(s) introduced by the refactoring ----
@SuppressWarnings("rawtypes")
private MaskedOnceArray[] getPartitions() {
    return (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
}

