private void addToIndex(DeferredReadRequest req) {
    for (BlockEntry entry : req.getEntries()) {
        BlockKey key = entry.getKey();
        Set<DeferredReadRequest> set = _byKey.get(key);
        if (set == null) {
            set = Collections.newSetFromMap(new IdentityHashMap<>());
            _byKey.put(key, set);
        }
        set.add(req);
    }
}