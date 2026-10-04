@SuppressWarnings("unchecked")
public SplittingOOCStream(OOCStream<T> sourceStream, Function<T, Integer> partitionFunc, int numPartitions) {
    _sourceStream = sourceStream;
    _subStreams = new SubOOCStream[numPartitions];
    for (int i = 0; i < numPartitions; i++) _subStreams[i] = new SubOOCStream<>(this);
    _sourceStream.setSubscriber(cb -> {
        try {
            try (cb) {
                if (cb.isFailure()) {
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
                    return;
                }
                if (cb.isEos()) {
                    SubOOCStream<T> current;
                    for (int i = 0; i < numPartitions; i++) {
                        // This requires no additional locking because we know EOS
                        // is always triggered after the last non EOS call finished
                        current = _subStreams[i];
                        if (current != null)
                            current.closeInput();
                    }
                    return;
                }
                if (cb instanceof OOCStream.GroupQueueCallback<?>) {
                    OOCStream.GroupQueueCallback<T> group = (OOCStream.GroupQueueCallback<T>) cb;
                    for (int gi = 0; gi < group.size(); gi++) {
                        OOCStream.QueueCallback<T> sub = group.getCallback(gi);
                        try (sub) {
                            int partition = partitionFunc.apply(sub.get());
                            if (partition < 0 || partition >= numPartitions)
                                throw new DMLRuntimeException("Invalid partition index: " + partition + " for " + numPartitions + " partitions");
                            _subStreams[partition].enqueue(sub.keepOpen());
                        }
                    }
                    return;
                }
                int partition = partitionFunc.apply(cb.get());
                if (partition < 0 || partition >= numPartitions)
                    throw new DMLRuntimeException("Invalid partition index: " + partition + " for " + numPartitions + " partitions");
                _subStreams[partition].enqueue(cb.keepOpen());
            }
        } catch (DMLRuntimeException re) {
            propagateFailure(re);
        }
    });
}