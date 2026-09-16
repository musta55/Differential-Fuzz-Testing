public Map<K, ContextualStorage> forceNewStorage() {
    Map<K, ContextualStorage> oldStorageMap = storageMap;
    storageMap = new ConcurrentHashMap<>();
    return oldStorageMap;
}