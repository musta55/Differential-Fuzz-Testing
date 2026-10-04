@Override
public SliceResult slice(int l, int u) {
    if (l == 0 && u > cols[cols.length - 1])
        return new SliceResult(0, cols.length, this);
    int s = Arrays.binarySearch(cols, l);
    int e = Arrays.binarySearch(cols, u);
    s = s < 0 ? Math.abs(s + 1) : s;
    e = e < 0 ? Math.abs(e + 1) : e;
    if (s == e)
        return new SliceResult(0, 0, null);
    int[] retArr = new int[e - s];
    if (l == 0)
        retArr = Arrays.copyOfRange(cols, s, e);
    else
        for (int i = s, j = 0; i < e; i++, j++) retArr[j] = cols[i] - l;
    SliceResult ret = new SliceResult(s, e, ColIndexFactory.create(retArr));
    return ret;
}