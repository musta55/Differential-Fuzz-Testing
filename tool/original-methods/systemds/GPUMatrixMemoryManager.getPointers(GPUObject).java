/**
 * Get list of all Pointers in a GPUObject
 * @param gObj gpu object
 * @return set of pointers
 */
Set<Pointer> getPointers(GPUObject gObj) {
    Set<Pointer> ret = new HashSet<>();
    if (!gObj.isDensePointerNull() && gObj.getSparseMatrixCudaPointer() != null) {
        LOG.warn("Matrix allocated in both dense and sparse format");
    }
    if (!gObj.isDensePointerNull()) {
        // && gObj.evictedDenseArr == null - Ignore evicted array
        ret.add(gObj.getDensePointer());
    }
    if (gObj.getSparseMatrixCudaPointer() != null) {
        CSRPointer sparsePtr = gObj.getSparseMatrixCudaPointer();
        if (sparsePtr != null) {
            if (sparsePtr.rowPtr != null)
                ret.add(sparsePtr.rowPtr);
            else if (sparsePtr.colInd != null)
                ret.add(sparsePtr.colInd);
            else if (sparsePtr.val != null)
                ret.add(sparsePtr.val);
        }
    }
    return ret;
}