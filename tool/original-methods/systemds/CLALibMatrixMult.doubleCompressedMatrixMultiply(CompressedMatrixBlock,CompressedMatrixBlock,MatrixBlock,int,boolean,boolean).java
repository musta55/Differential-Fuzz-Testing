private static MatrixBlock doubleCompressedMatrixMultiply(CompressedMatrixBlock m1, CompressedMatrixBlock m2, MatrixBlock ret, int k, boolean transposeLeft, boolean transposeRight) {
    if (!transposeLeft && !transposeRight) {
        // If both are not transposed, decompress the right hand side. to enable
        // compressed overlapping output.
        LOG.warn("Matrix decompression from multiplying two compressed matrices.");
        return matrixMultiply(m1, CompressedMatrixBlock.getUncompressed(m2), ret, k, transposeLeft, transposeRight);
    } else if (transposeLeft && !transposeRight) {
        if (m1.getNumColumns() > m2.getNumColumns()) {
            ret = CLALibLeftMultBy.leftMultByMatrixTransposed(m1, m2, ret, k);
            ReorgOperator r_op = new ReorgOperator(SwapIndex.getSwapIndexFnObject(), k);
            return ret.reorgOperations(r_op, new MatrixBlock(), 0, 0, 0);
        } else
            return CLALibLeftMultBy.leftMultByMatrixTransposed(m2, m1, ret, k);
    } else if (!transposeLeft && transposeRight) {
        throw new DMLCompressionException("Not Implemented compressed Matrix Mult to produce larger matrix");
        // worst situation since it blows up the result matrix in number of rows in
        // either compressed matrix.
    } else {
        ret = CLALibMatrixMult.matrixMultiply(m2, m1, ret, k);
        ReorgOperator r_op = new ReorgOperator(SwapIndex.getSwapIndexFnObject(), k);
        return ret.reorgOperations(r_op, new MatrixBlock(), 0, 0, 0);
    }
}