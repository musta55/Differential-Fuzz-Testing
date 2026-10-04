@Override
public void decompressToDenseFromSparse(SparseBlock sb, int vr, int off, double[] c) {
    if (sb instanceof SparseBlockCSR)
        decompressToDenseFromSparseCSR((SparseBlockCSR) sb, vr, off, c);
    else
        decompressToDenseFromSparseGeneric(sb, vr, off, c);
}