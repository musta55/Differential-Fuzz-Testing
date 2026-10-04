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
        ret.add(gObj.getDensePointer());
    }
    CSRPointer sparsePtr = gObj.getSparseMatrixCudaPointer();
    if (sparsePtr != null) {
        addNonNullPointer(ret, sparsePtr.rowPtr);
        addNonNullPointer(ret, sparsePtr.colInd);
        addNonNullPointer(ret, sparsePtr.val);
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

