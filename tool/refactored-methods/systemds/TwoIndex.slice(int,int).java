@Override
public SliceResult slice(int l, int u) {
    if (l <= id1 && u > id2)
        return new SliceResult(0, 2, l == 0 ? this : new TwoIndex(id1 - l, id2 - l));
    if (l <= id1 && u > id1)
        return new SliceResult(0, 1, new SingleIndex(id1 - l));
    if (l <= id2 && u > id2)
        return new SliceResult(1, 2, new SingleIndex(id2 - l));
    return new SliceResult(0, 0, null);
}