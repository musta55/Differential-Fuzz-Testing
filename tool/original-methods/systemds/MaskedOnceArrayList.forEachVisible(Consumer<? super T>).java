@SuppressWarnings({ "rawtypes", "unchecked" })
public void forEachVisible(Consumer<? super T> action) {
    MaskedOnceArray[] partitions = (MaskedOnceArray[]) PARTITIONS.getAcquire(this);
    for (int i = 0; i < partitions.length; i++) {
        MaskedOnceArray partition = (MaskedOnceArray) PARTITION.getAcquire(partitions, i);
        if (partition != null)
            partition.forEachVisible(action);
    }
}