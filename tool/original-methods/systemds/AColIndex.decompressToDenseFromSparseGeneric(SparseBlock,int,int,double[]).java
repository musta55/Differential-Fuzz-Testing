private void decompressToDenseFromSparseGeneric(SparseBlock sb, int vr, int off, double[] c) {
    if (sb.isEmpty(vr))
        return;
    final int apos = sb.pos(vr);
    final int alen = sb.size(vr) + apos;
    final int[] aix = sb.indexes(vr);
    final double[] aval = sb.values(vr);
    for (int j = apos; j < alen; j++) c[off + get(aix[j])] += aval[j];
}