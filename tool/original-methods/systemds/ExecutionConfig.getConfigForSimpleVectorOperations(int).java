/**
 * Use this for simple vector operations and use following in the kernel
 * <code>
 * int index = blockIdx.x * blockDim.x + threadIdx.x
 * </code>
 * <p>
 * This tries to schedule as minimum grids as possible.
 *
 * @param numCells number of cells
 * @return execution configuration
 */
public static ExecutionConfig getConfigForSimpleVectorOperations(int numCells) {
    if (numCells == 0)
        throw new DMLRuntimeException("Attempting to invoke a kernel with 0 threads");
    int deviceNumber = 0;
    int blockDimX = getMaxBlockDim(deviceNumber);
    int gridDimX = (int) Math.ceil((double) numCells / blockDimX);
    return new ExecutionConfig(gridDimX, blockDimX);
}