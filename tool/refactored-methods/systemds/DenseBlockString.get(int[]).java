@Override
public double get(int[] ix) {
    return parseDouble(_data[pos(ix)]);
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

