@Override
public DataCharacteristics setDimension(long nr, long nc) {
    _dims = new long[] { nr, nc };
    return this;
}