private static void removeEntry(Map<LineageItem, LineageCacheEntry> cache, LineageCacheEntry e) {
    if (e._origItem == null) {
        // Single entry. Remove.
        removeSingleEntry(cache, e);
        return;
    }
    // Defer the eviction till all the entries with the same intermediate are evicted.
    e.setCacheStatus(LineageCacheStatus.TODELETE);
    boolean del = false;
    LineageCacheEntry tmp = cache.get(e._origItem);
    while (tmp != null) {
        if (tmp.getCacheStatus() != LineageCacheStatus.TODELETE)
            //do nothing
            return;
        del |= (tmp.getCacheStatus() == LineageCacheStatus.TODELETE);
        tmp = tmp._nextEntry;
    }
    if (del) {
        tmp = cache.get(e._origItem);
        while (tmp != null) {
            removeSingleEntry(cache, tmp);
            tmp = tmp._nextEntry;
        }
    }
}