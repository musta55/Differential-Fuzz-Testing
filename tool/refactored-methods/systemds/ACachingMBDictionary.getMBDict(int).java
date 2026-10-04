@Override
public final MatrixBlockDictionary getMBDict(int nCol) {
    MatrixBlockDictionary cachedDict = getCachedDictionary();
    if (cachedDict != null) {
        return cachedDict;
    }
    MatrixBlockDictionary newDict = createMBDict(nCol);
    updateCache(newDict);
    return newDict;
}
// ---- helper method(s) introduced by the refactoring ----
private MatrixBlockDictionary getCachedDictionary() {
    if (cache != null) {
        return cache.get();
    }
    return null;
}

private void updateCache(MatrixBlockDictionary dict) {
    cache = new SoftReference<>(dict);
}

