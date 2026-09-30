@Override
public void endWindow() {
    cache.add(currentList);
    if (isCacheFull()) {
        slideCache();
    }
    unifier.endWindow();
}
// ---- helper method(s) introduced by the refactoring ----
private OutputPort<?> extractOutputPort() {
    for (Class<?> c = unifier.getClass(); c != Object.class; c = c.getSuperclass()) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object portObject = field.get(unifier);
                if (portObject instanceof OutputPort) {
                    return (OutputPort<?>) portObject;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    throw new RuntimeException("Unifier should have exactly one output port");
}

private boolean isCacheFull() {
    return cacheSize == numberOfBuckets - 1;
}

private void processCachedTuples() {
    for (List<Object> windowCache : cache) {
        for (Object obj : windowCache) {
            unifier.process(obj);
        }
    }
}

private void slideCache() {
    for (int i = 0; i < numberOfSlideBuckets; i++) {
        cache.remove(0);
    }
}

private void sleepForIdleTime() {
    try {
        Thread.sleep(spinMillis);
    } catch (InterruptedException ex) {
        throw new RuntimeException(ex);
    }
}

