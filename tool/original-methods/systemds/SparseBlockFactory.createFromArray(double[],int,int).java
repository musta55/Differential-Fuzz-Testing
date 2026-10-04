/**
 * Create a sparse block from an array. Note that the nnz count should be absolutely correct for this call to work.
 *
 * @param valsDense a double array of values linearized.
 * @param nCol The number of columns in reach row.
 * @param nnz  The number of non zero values.
 * @return A sparse block.
 */
public static SparseBlock createFromArray(final double[] valsDense, final int nCol, final int nnz) {
    final int nRow = valsDense.length / nCol;
    if (nnz > 0) {
        final int[] rowPtr = new int[nRow + 1];
        final int[] colIdx = new int[nnz];
        final double[] valsSparse = new double[nnz];
        int off = 0;
        for (int i = 0; i < valsDense.length; i++) {
            final int mod = i % nCol;
            if (mod == 0)
                rowPtr[i / nCol] = off;
            if (valsDense[i] != 0) {
                valsSparse[off] = valsDense[i];
                colIdx[off] = mod;
                off++;
            }
        }
        rowPtr[rowPtr.length - 1] = off;
        return new SparseBlockCSR(rowPtr, colIdx, valsSparse, nnz);
    } else {
        // empty MCSR block
        return new SparseBlockMCSR(nRow);
    }
}