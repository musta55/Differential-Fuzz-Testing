/**
 * Copy the nth row and return the dense pointer
 * @param n zero-based row index
 * @return dense pointer containing the nth row. This row is reused in the next iteration
 */
public Pointer getNthRow(int n) {
    if (isInputInSparseFormat) {
        resetAndSyncOutPointer();
        LibMatrixCUDA.sliceSparseDense(gCtx, instName, (CSRPointer) inPointer, outPointer, n, n, 0, LibMatrixCUDA.toInt(numColumns - 1), numColumns);
    } else {
        LibMatrixCUDA.sliceDenseDense(gCtx, instName, (Pointer) inPointer, outPointer, n, n, 0, LibMatrixCUDA.toInt(numColumns - 1), numColumns);
    }
    return outPointer;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetAndSyncOutPointer() {
    jcuda.runtime.JCuda.cudaDeviceSynchronize();
    cudaMemset(outPointer, 0, (long) numColumns * sizeOfDataType);
    jcuda.runtime.JCuda.cudaDeviceSynchronize();
}

