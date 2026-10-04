/**
 * Returns worst-case contiguous memory size
 * @param gpuObj gpu object
 * @return memory size in bytes
 */
long getWorstCaseContiguousMemorySize(GPUObject gpuObj) {
    long ret = 0;
    if (!gpuObj.isDensePointerNull() && !gpuObj.shadowBuffer.isBuffered()) {
        ret = gpuManager.allPointers.get(gpuObj.getDensePointer()).getSizeInBytes();
    } else if (gpuObj.getJcudaSparseMatrixPtr() != null && gpuObj.getJcudaSparseMatrixPtr().nnz > 0) {
        CSRPointer sparsePtr = gpuObj.getJcudaSparseMatrixPtr();
        if (sparsePtr.rowPtr != null) {
            ret = Math.max(ret, gpuManager.allPointers.get(sparsePtr.rowPtr).getSizeInBytes());
        }
        if (sparsePtr.colInd != null) {
            ret = Math.max(ret, gpuManager.allPointers.get(sparsePtr.colInd).getSizeInBytes());
        }
        if (sparsePtr.val != null) {
            ret = Math.max(ret, gpuManager.allPointers.get(sparsePtr.val).getSizeInBytes());
        }
    }
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private void addNonNullPointer(Set<Pointer> ret, Pointer ptr) {
    if (ptr != null) {
        ret.add(ptr);
    }
}

private Set<GPUObject> filterUnlockedGPUObjects() {
    return gpuObjects.stream().filter(gpuObj -> !gpuObj.isLocked()).collect(Collectors.toSet());
}

private void processUnlockedGPUObjects(Set<GPUObject> unlockedGPUObjects, String opcode) throws DMLRuntimeException {
    for (GPUObject toBeRemoved : unlockedGPUObjects) {
        if (toBeRemoved.dirty) {
            toBeRemoved.copyFromDeviceToHost(opcode, true, true);
        } else {
            toBeRemoved.clearData(opcode, true);
        }
    }
}

