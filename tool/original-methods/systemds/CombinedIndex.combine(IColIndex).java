@Override
public IColIndex combine(IColIndex other) {
    final int sr = other.size();
    final int sl = size();
    final int maxCombined = Math.max(this.get(this.size() - 1), other.get(other.size() - 1));
    final int minCombined = Math.min(this.get(0), other.get(0));
    if (sr + sl == maxCombined - minCombined + 1) {
        return new RangeIndex(minCombined, maxCombined + 1);
    }
    final int[] ret = new int[sr + sl];
    IIterate t = iterator();
    IIterate o = other.iterator();
    int i = 0;
    while (t.hasNext() && o.hasNext()) {
        final int tv = t.v();
        final int ov = o.v();
        if (tv < ov) {
            ret[i++] = tv;
            t.next();
        } else {
            ret[i++] = ov;
            o.next();
        }
    }
    while (t.hasNext()) ret[i++] = t.next();
    while (o.hasNext()) ret[i++] = o.next();
    return ColIndexFactory.create(ret);
}