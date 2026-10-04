public static long getSparseProductOutputNnz(MatrixBlock m1, MatrixBlock m2) {
    if (!m1.isInSparseFormat() || !m2.isInSparseFormat())
        throw new DMLRuntimeException("Invalid call to sparse output nnz estimation.");
    final int m = m1.getNumRows();
    final int n2 = m2.getNumColumns();
    long retNnz = 0;
    SparseBlock a = m1.getSparseBlock();
    SparseBlock b = m2.getSparseBlock();
    SparseRowVector tmpS = new SparseRowVector(1024);
    double[] tmpD = null;
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
        if (ldense) {
            //init dense tmp row
            tmpD = (tmpD == null) ? new double[n2] : tmpD;
            Arrays.fill(tmpD, 0);
        } else {
            tmpS.setSize(0);
        }
        for (int k = apos; k < apos + alen; k++) {
            if (b.isEmpty(aix[k]))
                continue;
            int blen = b.size(aix[k]);
            int bpos = b.pos(aix[k]);
            int[] bix = b.indexes(aix[k]);
            double aval = avals[k];
            double[] bvals = b.values(aix[k]);
            if (ldense) {
                //dense aggregation
                for (int j = bpos; j < bpos + blen; j++) tmpD[bix[j]] += aval * bvals[j];
            } else {
                //sparse aggregation
                for (int j = bpos; j < bpos + blen; j++) tmpS.add(bix[j], aval * bvals[j]);
            }
        }
        retNnz += !ldense ? tmpS.size() : UtilFunctions.computeNnz(tmpD, 0, n2);
    }
    return retNnz;
}