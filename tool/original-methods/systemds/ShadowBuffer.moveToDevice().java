/**
 * Move the data from shadow buffer to GPU
 */
public void moveToDevice() {
    long start = DMLScript.STATISTICS ? System.nanoTime() : 0;
    long numBytes = shadowPointer.length * LibMatrixCUDA.sizeOfDataType;
    gpuObj.jcudaDenseMatrixPtr = gpuObj.getGPUContext().allocate(null, numBytes, false);
    cudaMemcpy(gpuObj.jcudaDenseMatrixPtr, Pointer.to(shadowPointer), numBytes, jcuda.runtime.cudaMemcpyKind.cudaMemcpyHostToDevice);
    clearShadowPointer();
    if (DMLScript.STATISTICS) {
        long totalTime = System.nanoTime() - start;
        GPUStatistics.cudaFromShadowToDevTime.add(totalTime);
        GPUStatistics.cudaFromShadowToDevCount.increment();
    }
}