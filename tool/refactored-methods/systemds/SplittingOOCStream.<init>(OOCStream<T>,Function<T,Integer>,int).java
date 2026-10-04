@SuppressWarnings("unchecked")
public SplittingOOCStream(OOCStream<T> sourceStream, Function<T, Integer> partitionFunc, int numPartitions) {
    _sourceStream = sourceStream;
    initializeSubStreams(numPartitions);
    setSourceStreamSubscriber(partitionFunc, numPartitions);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeSubStreams(int numPartitions) {
    _subStreams = new SubOOCStream[numPartitions];
    for (int i = 0; i < numPartitions; i++) _subStreams[i] = new SubOOCStream<>(this);
}

private void setSourceStreamSubscriber(Function<T, Integer> partitionFunc, int numPartitions) {
    _sourceStream.setSubscriber(cb -> {
        try {
            try (cb) {
                handleCallback(cb, partitionFunc, numPartitions);
            }
        } catch (DMLRuntimeException re) {
            propagateFailure(re);
        }
    });
}

private void handleCallback(OOCStream.QueueCallback<T> cb, Function<T, Integer> partitionFunc, int numPartitions) {
    if (cb.isFailure()) {
        handleFailure(cb, numPartitions);
        return;
    }
    if (cb.isEos()) {
        handleEndOfStream(numPartitions);
        return;
    }
    if (cb instanceof OOCStream.GroupQueueCallback<?>) {
        handleGroupQueueCallback((OOCStream.GroupQueueCallback<T>) cb, partitionFunc, numPartitions);
        return;
    }
    handleSingleQueueCallback(cb, partitionFunc, numPartitions);
}

private void handleFailure(OOCStream.QueueCallback<T> cb, int numPartitions) {
    DMLRuntimeException failure;
    try {
        cb.get();
        failure = new DMLRuntimeException("Stream callback indicated failure without cause");
    } catch (DMLRuntimeException re) {
        failure = re;
    }
    for (int i = 0; i < numPartitions; i++) {
        SubOOCStream<T> current = _subStreams[i];
        if (current != null)
            current.propagateFailure(failure);
    }
}

private void handleEndOfStream(int numPartitions) {
    SubOOCStream<T> current;
    for (int i = 0; i < numPartitions; i++) {
        current = _subStreams[i];
        if (current != null)
            current.closeInput();
    }
}

private void handleGroupQueueCallback(OOCStream.GroupQueueCallback<T> group, Function<T, Integer> partitionFunc, int numPartitions) {
    for (int gi = 0; gi < group.size(); gi++) {
        OOCStream.QueueCallback<T> sub = group.getCallback(gi);
        try (sub) {
            int partition = partitionFunc.apply(sub.get());
            validatePartitionIndex(partition, numPartitions);
            _subStreams[partition].enqueue(sub.keepOpen());
        }
    }
}

private void handleSingleQueueCallback(OOCStream.QueueCallback<T> cb, Function<T, Integer> partitionFunc, int numPartitions) {
    int partition = partitionFunc.apply(cb.get());
    validatePartitionIndex(partition, numPartitions);
    _subStreams[partition].enqueue(cb.keepOpen());
}

private void validatePartitionIndex(int partition, int numPartitions) {
    if (partition < 0 || partition >= numPartitions)
        throw new DMLRuntimeException("Invalid partition index: " + partition + " for " + numPartitions + " partitions");
}

