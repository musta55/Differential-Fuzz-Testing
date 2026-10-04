/**
 * Move the data from shadow buffer to Matrix object
 */
public void moveToHost() {
    long start = DMLScript.STATISTICS ? System.nanoTime() : 0;
    MatrixBlock tmp = new MatrixBlock(GPUObject.toIntExact(gpuObj.mat.getNumRows()), GPUObject.toIntExact(gpuObj.mat.getNumColumns()), false);
    tmp.allocateDenseBlock();
    double[] tmpArr = tmp.getDenseBlockValues();
    for (int i = 0; i < shadowPointer.length; i++) {
        tmpArr[i] = shadowPointer[i];
    }
    gpuObj.mat.acquireModify(tmp);
    gpuObj.mat.release();
    clearShadowPointer();
    gpuObj.dirty = false;
    if (DMLScript.STATISTICS) {
        long totalTime = System.nanoTime() - start;
        GPUStatistics.cudaFromShadowToHostTime.add(totalTime);
        GPUStatistics.cudaFromShadowToHostCount.increment();
        // Part of dev -> host, not eviction
        GPUStatistics.cudaFromDevTime.add(totalTime);
        GPUStatistics.cudaFromDevCount.increment();
    }
}