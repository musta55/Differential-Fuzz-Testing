@Override
public void decompressToDenseFromSparse(SparseBlock sb, int vr, int off, double[] c) {
    if (sb instanceof SparseBlockCSR) {
        decompressToDenseFromSparseCSR((SparseBlockCSR) sb, vr, off, c);
    } else {
        decompressToDenseFromSparseGeneric(sb, vr, off, c);
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

