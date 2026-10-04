public boolean isPartitionedBroadcastValid() {
    return _pbcRef != null && checkPartitionedBroadcastValid();
}