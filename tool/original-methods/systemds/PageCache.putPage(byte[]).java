public static void putPage(byte[] data) {
    //cleanup if too many different size lists
    if (_pool.size() > CLEANUP_THRESHOLD)
        _pool.clear();
    LinkedList<SoftReference<byte[]>> list = _pool.get(data.length);
    if (list == null) {
        list = new LinkedList<>();
        _pool.put(data.length, list);
    }
    list.addLast(new SoftReference<>(data));
}