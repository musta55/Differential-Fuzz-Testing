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
    int pl = 0;
    int pr = 0;
    int i = 0;
    while (pl < sl && pr < sr) {
        final int vl = get(pl);
        final int vr = other.get(pr);
        if (vl < vr) {
            ret[i++] = vl;
            pl++;
        } else {
            ret[i++] = vr;
            pr++;
        }
    }
    while (pl < sl) ret[i++] = get(pl++);
    while (pr < sr) ret[i++] = other.get(pr++);
    return ColIndexFactory.create(ret);
}