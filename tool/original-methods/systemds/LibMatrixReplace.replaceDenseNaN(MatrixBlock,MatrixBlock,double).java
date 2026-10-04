private static long replaceDenseNaN(MatrixBlock in, MatrixBlock ret, double replacement) {
    DenseBlock a = in.getDenseBlock();
    DenseBlock c = ret.allocateDenseBlock().getDenseBlock();
    long nnz = 0;
    for (int bi = 0; bi < a.numBlocks(); bi++) {
        int len = a.size(bi);
        double[] avals = a.valuesAt(bi);
        double[] cvals = c.valuesAt(bi);
        for (int i = 0; i < len; i++) {
            cvals[i] = Double.isNaN(avals[i]) ? replacement : avals[i];
            nnz += cvals[i] != 0 ? 1 : 0;
        }
    }
    return nnz;
}