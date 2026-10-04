protected static void maintainOrder(LineageCacheEntry entry) {
    // Reset the timestamp to maintain the LRU component of the scoring function
    if (LineageCacheConfig.isTimeBased()) {
        if (weightedQueue.remove(entry)) {
            entry.updateTimestamp();
            weightedQueue.add(entry);
        }
    }
    // Scale score of the sought entry after every cache hit
    // FIXME: avoid when called from partial reuse methods
    if (LineageCacheConfig.isCostNsize()) {
        // Exists in weighted queue only if already marked for persistent
        if (weightedQueue.remove(entry)) {
            // Score stays same if not persisted (i.e. size == 0)
            entry.updateScore(true);
            weightedQueue.add(entry);
        }
    }
}