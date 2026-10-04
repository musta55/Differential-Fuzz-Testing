/**
 * Gets the available memory on GPU that SystemDS can use.
 *
 * @return the available memory in bytes
 */
@Override
public long getAvailableMemory() {
    updateMaxAvailableMemory();
    return maxAvailableMemory;
}
// ---- helper method(s) introduced by the refactoring ----
private synchronized void updateMaxAvailableMemory() {
    if (maxAvailableMemory < 0 || gpuUtilizationFactor != DMLScript.GPU_MEMORY_UTILIZATION_FACTOR) {
        long[] free = { 0 };
        long[] total = { 0 };
        cudaMemGetInfo(free, total);
        maxAvailableMemory = (long) (total[0] * DMLScript.GPU_MEMORY_UTILIZATION_FACTOR);
        gpuUtilizationFactor = DMLScript.GPU_MEMORY_UTILIZATION_FACTOR;
    }
}

