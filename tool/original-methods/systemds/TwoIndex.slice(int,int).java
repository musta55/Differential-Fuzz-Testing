@Override
public SliceResult slice(int l, int u) {
    SliceResult ret;
    if (l <= id1 && u > id2)
        ret = new SliceResult(0, 2, l == 0 ? this : new TwoIndex(id1 - l, id2 - l));
    else if (l <= id1 && u > id1)
        ret = new SliceResult(0, 1, new SingleIndex(id1 - l));
    else if (l <= id2 && u > id2)
        ret = new SliceResult(1, 2, new SingleIndex(id2 - l));
    else
        ret = new SliceResult(0, 0, null);
    return ret;
}