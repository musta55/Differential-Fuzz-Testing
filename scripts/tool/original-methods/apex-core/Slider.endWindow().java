@Override
public void endWindow() {
    cache.add(currentList);
    if (cacheSize == numberOfBuckets - 1) {
        for (int i = 0; i < numberOfSlideBuckets; i++) {
            cache.remove(0);
        }
    }
    unifier.endWindow();
}