public PartitionAwareSinkForPersistence(StreamCodecWrapperForPersistance<Object> serde, int mask, Sink<Object> output) {
    // If partition keys is null, everything should be passed to sink
    super(serde, createPartitionKeys(mask), mask, output);
    this.serdeForPersistence = serde;
}