protected static void rCleanupChildRDDs(LineageObject lob) {
    if (shouldAbortRecursiveCleanup(lob)) {
        return;
    }
    // Cleanup current lineage object (from driver/executors)
    SparkExecutionContext.cleanupSingleLineageObject(lob);
    //recursively process lineage children
    for (LineageObject c : lob.getLineageChilds()) {
        c.decrementNumReferences();
        rCleanupChildRDDs(c);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean shouldResetTimestamp(LineageCacheEntry entry) {
    return weightedQueue.remove(entry);
}

private static void resetTimestampAndRequeue(LineageCacheEntry entry) {
    entry.updateTimestamp();
    weightedQueue.add(entry);
}

private static boolean shouldUpdateScore(LineageCacheEntry entry) {
    return weightedQueue.remove(entry);
}

private static void updateScoreAndRequeue(LineageCacheEntry entry) {
    entry.updateScore(true);
    weightedQueue.add(entry);
}

private static boolean canDeleteEntries(Map<LineageItem, LineageCacheEntry> cache, LineageItem origItem) {
    LineageCacheEntry tmp = cache.get(origItem);
    while (tmp != null) {
        if (tmp.getCacheStatus() != LineageCacheStatus.TODELETE)
            return false;
        tmp = tmp._nextEntry;
    }
    return true;
}

private static void deleteEntries(Map<LineageItem, LineageCacheEntry> cache, LineageItem origItem) {
    LineageCacheEntry tmp = cache.get(origItem);
    while (tmp != null) {
        removeSingleEntry(cache, tmp);
        tmp = tmp._nextEntry;
    }
}

private static boolean shouldAbortRecursiveCleanup(LineageObject lob) {
    return lob.getNumReferences() > 0 || lob.hasBackReference() || isRDDYetToBePersisted(lob);
}

private static boolean isRDDYetToBePersisted(LineageObject lob) {
    return lob instanceof RDDObject && lob.isInLineageCache() && SparkExecutionContext.isRDDCached(((RDDObject) lob).getRDD().id());
}

