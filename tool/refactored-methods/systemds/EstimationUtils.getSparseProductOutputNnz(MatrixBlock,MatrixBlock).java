public static long getSparseProductOutputNnz(MatrixBlock m1, MatrixBlock m2) {
    if (!m1.isInSparseFormat() || !m2.isInSparseFormat())
        throw new DMLRuntimeException("Invalid call to sparse output nnz estimation.");
    final int m = m1.getNumRows();
    final int n2 = m2.getNumColumns();
    long retNnz = 0;
    SparseBlock a = m1.getSparseBlock();
    SparseBlock b = m2.getSparseBlock();
    for (int i = 0; i < m; i++) {
        if (a.isEmpty(i))
            continue;
        int alen = a.size(i);
        int apos = a.pos(i);
        int[] aix = a.indexes(i);
        double[] avals = a.values(i);
        //compute number of aggregated non-zeros for input row
        int nnz1 = (int) Math.min(UtilFunctions.computeNnz(b, aix, apos, alen), n2);
        boolean ldense = nnz1 > n2 / 128;
        //perform vector-matrix multiply w/ dense or sparse output
        retNnz += performVectorMatrixMultiply(b, aix, apos, alen, avals, n2, ldense);
    }
    return retNnz;
}
// ---- helper method(s) introduced by the refactoring ----
private static long performVectorMatrixMultiply(SparseBlock block, int[] indexes, int pos, int len, double[] values, int n, boolean ldense) {
    SparseRowVector tmpS = new SparseRowVector(1024);
    double[] tmpD = null;
    if (ldense) {
        //init dense tmp row
        tmpD = (tmpD == null) ? new double[n] : tmpD;
        Arrays.fill(tmpD, 0);
    } else {
        tmpS.setSize(0);
    }
    for (int k = pos; k < pos + len; k++) {
        if (block.isEmpty(indexes[k]))
            continue;
        int blen = block.size(indexes[k]);
        int bpos = block.pos(indexes[k]);
        int[] bix = block.indexes(indexes[k]);
        double aval = values[k];
        double[] bvals = block.values(indexes[k]);
        if (ldense) {
            //dense aggregation
            for (int j = bpos; j < bpos + blen; j++) tmpD[bix[j]] += aval * bvals[j];
        } else {
            //sparse aggregation
            for (int j = bpos; j < bpos + blen; j++) tmpS.add(bix[j], aval * bvals[j]);
        }
    }
    return !ldense ? tmpS.size() : UtilFunctions.computeNnz(tmpD, 0, n);
}

