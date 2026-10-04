/**
 * Create a reader of the matrix block that is able to iterate though all the rows and return as dense double
 * arrays.
 *
 * Note the reader reuse the return, therefore if needed for something please copy the returned rows.
 *
 * @param rawBlock   The block to iterate though
 * @param colIndices The column indexes to extract and insert into the double array
 * @param transposed If the raw block should be treated as transposed
 * @return A reader of the columns specified
 */
public static ReaderColumnSelection createReader(MatrixBlock rawBlock, IColIndex colIndices, boolean transposed) {
    final int rl = 0;
    final int ru = transposed ? rawBlock.getNumColumns() : rawBlock.getNumRows();
    return createReader(rawBlock, colIndices, transposed, rl, ru);
}