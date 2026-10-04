/**
 * Checks if the GPU object is eligible for shadow buffering
 *
 * @param isEviction true if this method is called during eviction
 * @param eagerDelete true if the data on device has to be eagerly deleted
 * @return true if the given GPU object is eligible to be shadow buffered
 */
public boolean isEligibleForBuffering(boolean isEviction, boolean eagerDelete) {
    if (LibMatrixCUDA.sizeOfDataType == jcuda.Sizeof.FLOAT && isEviction && eagerDelete && !gpuObj.isDensePointerNull()) {
        int numBytes = GPUObject.toIntExact(gpuObj.mat.getNumRows() * gpuObj.mat.getNumColumns()) * Sizeof.FLOAT;
        boolean ret = DMLScript.EVICTION_SHADOW_BUFFER_CURR_BYTES + numBytes <= DMLScript.EVICTION_SHADOW_BUFFER_MAX_BYTES;
        if (!ret && !_warnedAboutShadowBuffer) {
            LOG.warn("Shadow buffer is full, so using CP bufferpool instead. Consider increasing sysds.gpu.eviction.shadow.bufferSize.");
            _warnedAboutShadowBuffer = true;
        }
        return ret;
    } else {
        return false;
    }
}