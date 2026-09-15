@Override
public void process(Object tuple) {
    if (cacheSize == numberOfBuckets - 1) {
        unifier.process(tuple);
    }
    currentList.add(tuple);
}