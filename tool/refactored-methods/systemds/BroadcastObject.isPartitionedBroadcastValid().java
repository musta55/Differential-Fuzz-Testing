public boolean isPartitionedBroadcastValid() {
    return _pbcRef != null && isSoftReferenceValid(_pbcRef) && areIndividualBroadcastsValid(getPartitionedBroadcast());
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSoftReferenceValid(SoftReference<?> ref) {
    return ref.get() != null;
}

private boolean areIndividualBroadcastsValid(PartitionedBroadcast<T> pbm) {
    if (pbm == null)
        return false;
    Broadcast<PartitionedBlock<T>>[] tmp = pbm.getBroadcasts();
    for (Broadcast<PartitionedBlock<T>> bc : tmp) if (!bc.isValid())
        return false;
    return true;
}

