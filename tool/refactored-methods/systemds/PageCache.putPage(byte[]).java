public static void putPage(byte[] data) {
    cleanupIfNecessary();
    LinkedList<SoftReference<byte[]>> list = _pool.get(data.length);
    if (list == null) {
        list = new LinkedList<>();
        _pool.put(data.length, list);
    }
    list.addLast(new SoftReference<>(data));
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

