private void decompressToDenseFromSparseCSR(SparseBlockCSR sb, int vr, int off, double[] c) {
    int apos = sb.pos(vr);
    int alen = sb.size(vr) + apos;
    int[] aix = sb.indexes(vr);
    double[] aval = sb.values(vr);
    for (int j = apos; j < alen; j++) {
        c[off + get(aix[j])] += aval[j];
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean allIndicesPresent(IColIndex index) {
    IIterate iterator = index.iterator();
    while (iterator.hasNext()) {
        if (findIndex(iterator.next()) < 0) {
            return false;
        }
    }
    return true;
}

