@Override
public DenseBlock set(DenseBlock db) {
    int[] ix = new int[numDims()];
    for (int r = 0; r < _rlen; r++) {
        ix[0] = r;
        setRow(db, ix);
    }
    return this;
}
// ---- helper method(s) introduced by the refactoring ----
private void setRow(DenseBlock db, int[] ix) {
    for (int c = 0; c < _odims[0]; c++) {
        // for linear scan
        ix[ix.length - 1] = c;
        _data[pos(ix)] = db.getString(ix);
    }
}

private double parseDouble(String s) {
    return s == null || s.isEmpty() ? 0 : Double.parseDouble(s);
}

