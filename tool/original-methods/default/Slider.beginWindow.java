@Override
public void beginWindow(long windowId) {
    cacheSize = cache.size();
    unifier.beginWindow(windowId);
    if (cacheSize == numberOfBuckets - 1) {
        for (List<Object> windowCache : cache) {
            for (Object obj : windowCache) {
                unifier.process(obj);
            }
        }
    }
    currentList = new LinkedList<>();
}