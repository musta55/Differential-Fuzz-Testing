public static byte[] getPage(int size) {
    LinkedList<SoftReference<byte[]>> list = _pool.get(size);
    if (list != null) {
        while (!list.isEmpty()) {
            SoftReference<byte[]> ref = list.removeFirst();
            byte[] tmp = ref.get();
            if (tmp != null)
                return tmp;
        }
    }
    return null;
}