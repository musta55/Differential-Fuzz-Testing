/**
 * Convert the metadata to a DataCharacteristics object. If all field
 * values are {@code null}, {@code null} is returned.
 *
 * @return the metadata as a DataCharacteristics object, or {@code null}
 *         if all field values are null
 */
public MatrixCharacteristics asMatrixCharacteristics() {
    if (numRows == null && numColumns == null && blockSize == null && numNonZeros == null) {
        return null;
    }
    long nr = (numRows == null) ? -1 : numRows;
    long nc = (numColumns == null) ? -1 : numColumns;
    int blen = (blockSize == null) ? ConfigurationManager.getBlocksize() : blockSize;
    long nnz = (numNonZeros == null) ? -1 : numNonZeros;
    return new MatrixCharacteristics(nr, nc, blen, nnz);
}