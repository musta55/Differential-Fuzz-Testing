/**
 * This utility function computes the exact output nnz
 * of a self matrix product without need to materialize
 * the output.
 *
 * @param m1 dense or sparse input matrix
 * @return exact output number of non-zeros.
 */
public static long getSelfProductOutputNnz(MatrixBlock m1) {
    final int m = m1.getNumRows();
    final int n = m1.getNumColumns();
    long retNnz = 0;
    if (m1.isInSparseFormat()) {
        SparseBlock a = m1.getSparseBlock();
        for (int i = 0; i < m; i++) {
            if (a.isEmpty(i))
                continue;
            int alen = a.size(i);
            int apos = a.pos(i);
            int[] aix = a.indexes(i);
            double[] avals = a.values(i);
            //compute number of aggregated non-zeros for input row
            int nnz1 = (int) Math.min(UtilFunctions.computeNnz(a, aix, apos, alen), n);
            boolean ldense = nnz1 > n / 128;
            //perform vector-matrix multiply w/ dense or sparse output
            retNnz += performVectorMatrixMultiply(a, aix, apos, alen, avals, n, ldense);
        }
    } else {
        //dense
        DenseBlock a = m1.getDenseBlock();
        double[] tmp = new double[n];
        for (int i = 0; i < m; i++) {
            double[] avals = a.values(i);
            int aix = a.pos(i);
            //reset
            Arrays.fill(tmp, 0);
            for (int k = 0; k < n; k++) {
                double aval = avals[aix + k];
                if (aval == 0)
                    continue;
                double[] bvals = a.values(k);
                int bix = a.pos(k);
                for (int j = 0; j < n; j++) tmp[j] += aval * bvals[bix + j];
            }
            retNnz += UtilFunctions.computeNnz(tmp, 0, n);
        }
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

