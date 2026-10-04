@Override
public DblArrayCountHashMap clone() {
    DblArrayCountHashMap ret = new DblArrayCountHashMap(size);
    cloneData(ret);
    ret.size = size;
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private void cloneData(DblArrayCountHashMap ret) {
    for (ACount<DblArray> e : data) ret.appendValue(e);
}

