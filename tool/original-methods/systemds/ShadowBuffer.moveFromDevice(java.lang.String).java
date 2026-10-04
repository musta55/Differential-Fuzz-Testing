/**
 * Move the data from GPU to shadow buffer
 * @param instName name of the instruction
 */
public void moveFromDevice(String instName) {
    long start = DMLScript.STATISTICS ? System.nanoTime() : 0;
    int numElems = GPUObject.toIntExact(gpuObj.mat.getNumRows() * gpuObj.mat.getNumColumns());
    shadowPointer = new float[numElems];
    DMLScript.EVICTION_SHADOW_BUFFER_CURR_BYTES += shadowPointer.length * Sizeof.FLOAT;
    cudaMemcpy(Pointer.to(shadowPointer), gpuObj.jcudaDenseMatrixPtr, numElems * LibMatrixCUDA.sizeOfDataType, jcuda.runtime.cudaMemcpyKind.cudaMemcpyDeviceToHost);
    gpuObj.getGPUContext().cudaFreeHelper(instName, gpuObj.jcudaDenseMatrixPtr, true);
    gpuObj.jcudaDenseMatrixPtr = null;
    if (DMLScript.STATISTICS) {
        // Eviction time measure in malloc
        long totalTime = System.nanoTime() - start;
        GPUStatistics.cudaFromDevToShadowTime.add(totalTime);
        GPUStatistics.cudaFromDevToShadowCount.increment();
    }
}