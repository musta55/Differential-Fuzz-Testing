@Override
public int compareTo(FederatedRange o) {
    for (int i = 0; i < _beginDims.length; i++) {
        int cmp = compareDimension(_beginDims[i], o._beginDims[i]);
        if (cmp != 0) {
            return cmp;
        }
        cmp = compareDimension(_endDims[i], o._endDims[i]);
        if (cmp != 0) {
            return cmp;
        }
    }
    return 0;
}
// ---- helper method(s) introduced by the refactoring ----
private int compareDimension(long a, long b) {
    if (a < b) {
        return -1;
    }
    if (a > b) {
        return 1;
    }
    return 0;
}

