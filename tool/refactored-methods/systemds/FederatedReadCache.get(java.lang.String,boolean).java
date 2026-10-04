/**
 * Get the data from the ReadCacheEntry corresponding to the specified
 * filename, if the data from this filename has already been read.
 * Otherwise, create a new ReadCacheEntry for the filename and return null
 * to indicate that the data is not cached yet.
 *
 * @param fname the filename of the read data
 * @param putPlaceholder whether to put a placeholder if there is no mapping for the filename
 * @return the CacheableData object if it is cached, otherwise null
 */
public CacheableData<?> get(String fname, boolean putPlaceholder) {
    ReadCacheEntry entry = getEntry(fname, putPlaceholder);
    return (entry != null) ? entry.get() : null;
}
// ---- helper method(s) introduced by the refactoring ----
private ReadCacheEntry getEntry(String fname, boolean putPlaceholder) {
    return putPlaceholder ? _rmap.putIfAbsent(fname, new ReadCacheEntry()) : _rmap.get(fname);
}

