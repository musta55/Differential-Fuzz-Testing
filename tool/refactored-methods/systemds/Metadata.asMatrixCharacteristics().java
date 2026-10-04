/**
 * Convert the metadata to a DataCharacteristics object. If all field
 * values are {@code null}, {@code null} is returned.
 *
 * @return the metadata as a DataCharacteristics object, or {@code null}
 *         if all field values are null
 */
public MatrixCharacteristics asMatrixCharacteristics() {
    if (areAllFieldsNull()) {
        return null;
    }
    return createMatrixCharacteristics();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean areAllFieldsNull() {
    return numRows == null && numColumns == null && blockSize == null && numNonZeros == null;
}

private MatrixCharacteristics createMatrixCharacteristics() {
    long nr = getValueOrDefault(numRows, -1);
    long nc = getValueOrDefault(numColumns, -1);
    int blen = getValueOrDefault(blockSize, ConfigurationManager.getBlocksize());
    long nnz = getValueOrDefault(numNonZeros, -1);
    return new MatrixCharacteristics(nr, nc, blen, nnz);
}

private long getValueOrDefault(Long value, long defaultValue) {
    return value != null ? value : defaultValue;
}

private int getValueOrDefault(Integer value, int defaultValue) {
    return value != null ? value : defaultValue;
}

