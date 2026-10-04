/**
 * Returns worst-case contiguous memory size
 * @param gpuObj gpu object
 * @return memory size in bytes
 */
long getWorstCaseContiguousMemorySize(GPUObject gpuObj) {
    long ret = 0;
    if (!gpuObj.isDensePointerNull()) {
        if (!gpuObj.shadowBuffer.isBuffered())
            ret = gpuManager.allPointers.get(gpuObj.getDensePointer()).getSizeInBytes();
        else
            // evicted hence no contiguous memory on GPU
            ret = 0;
    } else if (gpuObj.getJcudaSparseMatrixPtr() != null) {
        CSRPointer sparsePtr = gpuObj.getJcudaSparseMatrixPtr();
        if (sparsePtr.nnz > 0) {
            if (sparsePtr.rowPtr != null)
                ret = Math.max(ret, gpuManager.allPointers.get(sparsePtr.rowPtr).getSizeInBytes());
            if (sparsePtr.colInd != null)
                ret = Math.max(ret, gpuManager.allPointers.get(sparsePtr.colInd).getSizeInBytes());
            if (sparsePtr.val != null)
                ret = Math.max(ret, gpuManager.allPointers.get(sparsePtr.val).getSizeInBytes());
        }
    }
    return ret;
}