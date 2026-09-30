public PartitionAwareSinkForPersistence(StreamCodecWrapperForPersistance<Object> serde, Set<Integer> partitions, int mask, Sink<Object> output) {
    super(serde, partitions, mask, output);
    this.serdeForPersistence = serde;
}