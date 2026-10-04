private void addToIndex(DeferredReadRequest req) {
    for (BlockEntry entry : req.getEntries()) {
        BlockKey key = entry.getKey();
        Set<DeferredReadRequest> set = getOrCreateSet(key);
        set.add(req);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Set<DeferredReadRequest> getOrCreateSet(BlockKey key) {
    Set<DeferredReadRequest> set = _byKey.get(key);
    if (set == null) {
        set = Collections.newSetFromMap(new IdentityHashMap<>());
        _byKey.put(key, set);
    }
    return set;
}

