public static CmCovObject centralMoment(CompressedMatrixBlock cmb, CMOperator op) {
    MatrixBlock.checkCMOperations(cmb, op);
    if (cmb.isEmpty())
        return LibMatrixAgg.aggregateCmCov(cmb, null, null, op.fn);
    else if (cmb.isOverlapping())
        return cmb.getUncompressed("cmOperations on overlapping state", op.getNumThreads()).cmOperations(op);
    final List<AColGroup> groups = cmb.getColGroups();
    if (groups.size() == 1)
        return groups.get(0).centralMoment(op, cmb.getNumRows());
    return cmb.getUncompressed("Decompressing but should never happen that a single column is non overlapping and contain multiple groups", op.getNumThreads()).cmOperations(op);
}