private static void prepNonZerosForMatrixMult(MatrixBlock mb, boolean update) {
    if (!update)
        return;
    //non-zeros are not evaluated for dense matrix multiplies
    //so we simply need to ensure the block is not marked empty
    if (!mb.isInSparseFormat())
        mb.setNonZeros((long) mb.getNumRows() * mb.getNumColumns());
    else
        mb.recomputeNonZeros();
}