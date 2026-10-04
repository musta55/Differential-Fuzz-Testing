public static CmCovObject centralMoment(CompressedMatrixBlock cmb, CMOperator op) {
    MatrixBlock.checkCMOperations(cmb, op);
    if (cmb.isEmpty()) {
        return handleEmptyMatrix(cmb, op);
    } else if (cmb.isOverlapping()) {
        return handleOverlappingMatrix(cmb, op);
    }
    return handleNonOverlappingMatrix(cmb, op);
}
// ---- helper method(s) introduced by the refactoring ----
private static CmCovObject handleEmptyMatrix(CompressedMatrixBlock cmb, CMOperator op) {
    return LibMatrixAgg.aggregateCmCov(cmb, null, null, op.fn);
}

private static CmCovObject handleOverlappingMatrix(CompressedMatrixBlock cmb, CMOperator op) {
    return cmb.getUncompressed("cmOperations on overlapping state", op.getNumThreads()).cmOperations(op);
}

private static CmCovObject handleNonOverlappingMatrix(CompressedMatrixBlock cmb, CMOperator op) {
    List<AColGroup> groups = cmb.getColGroups();
    if (groups.size() == 1) {
        return groups.get(0).centralMoment(op, cmb.getNumRows());
    }
    return cmb.getUncompressed("Decompressing but should never happen that a single column is non overlapping and contain multiple groups", op.getNumThreads()).cmOperations(op);
}

