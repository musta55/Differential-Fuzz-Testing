public static byte[] getPage(int size) {
    LinkedList<SoftReference<byte[]>> list = _pool.get(size);
    if (list != null) {
        return retrievePageFromList(list);
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private static void cleanupIfNecessary() {
    if (_pool.size() > CLEANUP_THRESHOLD)
        _pool.clear();
}

private static byte[] retrievePageFromList(LinkedList<SoftReference<byte[]>> list) {
    while (!list.isEmpty()) {
        SoftReference<byte[]> ref = list.removeFirst();
        byte[] tmp = ref.get();
        if (tmp != null)
            return tmp;
    }
    return null;
}

