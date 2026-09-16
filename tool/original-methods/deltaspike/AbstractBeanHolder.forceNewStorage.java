public Map<K, ContextualStorage> forceNewStorage() {
    Map<K, ContextualStorage> oldStorageMap = storageMap;
    storageMap = new ConcurrentHashMap<K, ContextualStorage>();
    return oldStorageMap;
}