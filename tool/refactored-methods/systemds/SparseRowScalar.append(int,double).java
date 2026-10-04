@Override
public SparseRow append(int col, double v) {
    if (v == 0)
        return this;
    else if (index >= 0) {
        // if already set
        SparseRowVector srv = new SparseRowVector();
        srv.append(index, value);
        srv.append(col, v);
        return srv;
    } else {
        return appendToEmpty(col, v);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private SparseRow appendToEmpty(int col, double v) {
    index = col;
    value = v;
    return this;
}

