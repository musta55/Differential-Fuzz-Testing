public static MatrixBlock ternaryOperations(TernaryOperator op, MatrixBlock m1, MatrixBlock m2, MatrixBlock m3) {
    // get the input dimensions.
    final int r1 = m1.getNumRows();
    final int r2 = m2.getNumRows();
    final int r3 = m3.getNumRows();
    final int c1 = m1.getNumColumns();
    final int c2 = m2.getNumColumns();
    final int c3 = m3.getNumColumns();
    // empty or scalar constants to be used.
    final boolean s1 = (r1 == 1 && c1 == 1) || m1.isEmpty();
    final boolean s2 = (r2 == 1 && c2 == 1) || m2.isEmpty();
    final boolean s3 = (r3 == 1 && c3 == 1) || m3.isEmpty();
    final double d1 = s1 ? m1.get(0, 0) : Double.NaN;
    final double d2 = s2 ? m2.get(0, 0) : Double.NaN;
    final double d3 = s3 ? m3.get(0, 0) : Double.NaN;
    // Get the dimensions of the output.
    final int m = Math.max(Math.max(r1, r2), r3);
    final int n = Math.max(Math.max(c1, c2), c3);
    // double check that the dimensions are valid.
    MatrixBlock.ternaryOperationCheck(s1, s2, s3, m, r1, r2, r3, n, c1, c2, c3);
    MatrixBlock ret;
    final boolean PM_Or_MM = (op.fn instanceof PlusMultiply || op.fn instanceof MinusMultiply);
    if (s1 && s2 && s3) {
        // all empty or scalar constant.
        double v = op.fn.execute(d1, d2, d3);
        return CompressedMatrixBlockFactory.createConstant(m, n, v);
    }
    if (PM_Or_MM) {
        if (((s2 && d2 == 0) || (s3 && d3 == 0))) {
            if (m1 instanceof CompressedMatrixBlock)
                ret = new CompressedMatrixBlock();
            else
                ret = new MatrixBlock();
            ret.copy(m1);
            return ret;
        } else if ((s2 && s3) || (s1 && s2) || (s1 && s3)) {
            LOG.debug("Ternary operator could be converted to scalar because of constant sides");
        } else if (s2 || s3) {
            BinaryOperator bop = op.setOp2Constant(s2 ? d2 : d3);
            ret = m1.binaryOperations(bop, s2 ? m3 : m2);
            return ret;
        }
    }
    // decompress any compressed matrix.
    m1 = decompress(op, m1, 1);
    m2 = decompress(op, m2, 2);
    m3 = decompress(op, m3, 3);
    ret = new MatrixBlock();
    final boolean sparseOutput = MatrixBlock.evalSparseFormatInMemory(m, n, (s1 ? m * n * (d1 != 0 ? 1 : 0) : m1.getNonZeros()) + Math.min(s2 ? m * n : m2.getNonZeros(), s3 ? m * n : m3.getNonZeros()));
    ret.reset(m, n, sparseOutput);
    LibMatrixTercell.tercellOp(m1, m2, m3, ret, op);
    ret.examSparsity();
    return ret;
}