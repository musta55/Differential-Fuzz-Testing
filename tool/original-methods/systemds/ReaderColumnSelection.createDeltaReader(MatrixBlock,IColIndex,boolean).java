/**
 * Create a reader of the matrix block that computes delta values (current row - previous row) on-the-fly.
 *
 * Note the reader reuse the return, therefore if needed for something please copy the returned rows.
 * The first row is returned as-is (no delta computation).
 *
 * @param rawBlock   The block to iterate though
 * @param colIndices The column indexes to extract and insert into the double array
 * @param transposed If the raw block should be treated as transposed
 * @return A delta reader of the columns specified
 */
public static ReaderColumnSelection createDeltaReader(MatrixBlock rawBlock, IColIndex colIndices, boolean transposed) {
    final int rl = 0;
    final int ru = transposed ? rawBlock.getNumColumns() : rawBlock.getNumRows();
    return createDeltaReader(rawBlock, colIndices, transposed, rl, ru);
}