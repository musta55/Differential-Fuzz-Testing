private static long replaceDense(MatrixBlock in, MatrixBlock ret, double pattern, double replacement) {
    DenseBlock a = in.getDenseBlock();
    DenseBlock c = ret.allocateDenseBlock().getDenseBlock();
    long nnz = 0;
    for (int bi = 0; bi < a.numBlocks(); bi++) {
        int len = a.size(bi);
        double[] avals = a.valuesAt(bi);
        double[] cvals = c.valuesAt(bi);
        for (int i = 0; i < len; i++) {
            cvals[i] = avals[i] == pattern ? replacement : avals[i];
            nnz += cvals[i] != 0 ? 1 : 0;
        }
    }
    return nnz;
}