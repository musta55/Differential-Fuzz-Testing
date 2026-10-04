@Override
public final MatrixBlockDictionary getMBDict(int nCol) {
    if (cache != null) {
        MatrixBlockDictionary r = cache.get();
        if (r != null)
            return r;
    }
    MatrixBlockDictionary ret = createMBDict(nCol);
    cache = new SoftReference<>(ret);
    return ret;
}