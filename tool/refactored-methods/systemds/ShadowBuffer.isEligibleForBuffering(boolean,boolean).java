/**
 * Checks if the GPU object is eligible for shadow buffering
 *
 * @param isEviction true if this method is called during eviction
 * @param eagerDelete true if the data on device has to be eagerly deleted
 * @return true if the given GPU object is eligible to be shadow buffered
 */
public boolean isEligibleForBuffering(boolean isEviction, boolean eagerDelete) {
    if (isEligibilityConditionsMet(isEviction, eagerDelete)) {
        long numBytes = calculateRequiredBytes();
        boolean isEligible = checkBufferCapacity(numBytes);
        logWarningIfNotEligible(isEligible);
        return isEligible;
    } else {
        return false;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private int allocateShadowBuffer() {
    int numElements = GPUObject.toIntExact(gpuObj.mat.getNumRows() * gpuObj.mat.getNumColumns());
    shadowPointer = new float[numElements];
    DMLScript.EVICTION_SHADOW_BUFFER_CURR_BYTES += shadowPointer.length * Sizeof.FLOAT;
    return numElements;
}

private void copyDataFromDevice(int numElements) {
    cudaMemcpy(Pointer.to(shadowPointer), gpuObj.jcudaDenseMatrixPtr, numElements * LibMatrixCUDA.sizeOfDataType, jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost);
}

private void freeGPUMemory(String instName) {
    gpuObj.getGPUContext().cudaFreeHelper(instName, gpuObj.jcudaDenseMatrixPtr, true);
    gpuObj.jcudaDenseMatrixPtr = null;
}

private void recordMoveFromDeviceTime(long startTime) {
    long totalTime = System.nanoTime() - startTime;
    GPUStatistics.cudaFromDevToShadowTime.add(totalTime);
    GPUStatistics.cudaFromDevToShadowCount.increment();
}

private MatrixBlock createMatrixBlock() {
    return new MatrixBlock(GPUObject.toIntExact(gpuObj.mat.getNumRows()), GPUObject.toIntExact(gpuObj.mat.getNumColumns()), false);
}

private void copyDataToMatrixBlock(MatrixBlock matrixBlock) {
    matrixBlock.allocateDenseBlock();
    double[] denseBlockValues = matrixBlock.getDenseBlockValues();
    for (int i = 0; i < shadowPointer.length; i++) {
        denseBlockValues[i] = shadowPointer[i];
    }
}

private void updateGPUObject(MatrixBlock matrixBlock) {
    gpuObj.mat.acquireModify(matrixBlock);
    gpuObj.mat.release();
    clearShadowPointer();
    gpuObj.dirty = false;
}

private void recordMoveToHostTime(long startTime) {
    long totalTime = System.nanoTime() - startTime;
    GPUStatistics.cudaFromShadowToHostTime.add(totalTime);
    GPUStatistics.cudaFromShadowToHostCount.increment();
    GPUStatistics.cudaFromDevTime.add(totalTime);
    GPUStatistics.cudaFromDevCount.increment();
}

private long calculateNumBytes() {
    return shadowPointer.length * LibMatrixCUDA.sizeOfDataType;
}

private void allocateGPUMemory(long numBytes) {
    gpuObj.jcudaDenseMatrixPtr = gpuObj.getGPUContext().allocate(null, numBytes, false);
}

private void copyDataToDevice(long numBytes) {
    cudaMemcpy(gpuObj.jcudaDenseMatrixPtr, Pointer.to(shadowPointer), numBytes, jcuda.runtime.cudaMemcpyKind.cudaMemcpyHostToDevice);
    clearShadowPointer();
}

private void recordMoveToDeviceTime(long startTime) {
    long totalTime = System.nanoTime() - startTime;
    GPUStatistics.cudaFromShadowToDevTime.add(totalTime);
    GPUStatistics.cudaFromShadowToDevCount.increment();
}

private boolean isEligibilityConditionsMet(boolean isEviction, boolean eagerDelete) {
    return LibMatrixCUDA.sizeOfDataType == jcuda.Sizeof.FLOAT && isEviction && eagerDelete && !gpuObj.isDensePointerNull();
}

private long calculateRequiredBytes() {
    return GPUObject.toIntExact(gpuObj.mat.getNumRows() * gpuObj.mat.getNumColumns()) * Sizeof.FLOAT;
}

private boolean checkBufferCapacity(long numBytes) {
    return DMLScript.EVICTION_SHADOW_BUFFER_CURR_BYTES + numBytes <= DMLScript.EVICTION_SHADOW_BUFFER_MAX_BYTES;
}

private void logWarningIfNotEligible(boolean isEligible) {
    if (!isEligible && !_warnedAboutShadowBuffer) {
        LOG.warn("Shadow buffer is full, so using CP bufferpool instead. Consider increasing sysds.gpu.eviction.shadow.bufferSize.");
        _warnedAboutShadowBuffer = true;
    }
}

