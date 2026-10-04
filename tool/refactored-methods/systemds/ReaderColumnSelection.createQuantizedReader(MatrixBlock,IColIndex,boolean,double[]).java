/**
 * Create a reader of the matrix block that directly reads quantized values using scale factors.
 *
 * Note the reader reuse the return, therefore if needed for something please copy the returned rows.
 *
 * @param rawBlock   	The block to iterate though
 * @param colIndices 	The column indexes to extract and insert into the double array
 * @param transposed 	If the raw block should be treated as transposed
 * @param scaleFactors 	An array of scale factors applied.
 *                     	- If row-wise scaling is used, this should be an array where each value corresponds to a row.
 *                     	- If a single scalar is provided, it is applied uniformly to the entire matrix.
 * @return A reader of the columns specified
 */
public static ReaderColumnSelection createQuantizedReader(MatrixBlock rawBlock, IColIndex colIndices, boolean transposed, double[] scaleFactors) {
    if (transposed) {
        throw new NotImplementedException();
    }
    return createQuantizedReader(rawBlock, colIndices, transposed, 0, transposed ? rawBlock.getNumColumns() : rawBlock.getNumRows(), scaleFactors);
}