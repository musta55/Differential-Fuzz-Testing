/**
 * Clear all unlocked gpu objects that are not lineage cached
 *
 * @param opcode instruction code
 * @throws DMLRuntimeException if error
 */
void clearAllUnlocked(String opcode) throws DMLRuntimeException {
    Set<GPUObject> unlockedGPUObjects = gpuObjects.stream().filter(gpuObj -> !gpuObj.isLocked()).collect(Collectors.toSet());
    if (unlockedGPUObjects.size() > 0) {
        if (LOG.isWarnEnabled())
            LOG.warn("Clearing all unlocked matrices (count=" + unlockedGPUObjects.size() + ").");
        for (GPUObject toBeRemoved : unlockedGPUObjects) {
            if (toBeRemoved.dirty)
                toBeRemoved.copyFromDeviceToHost(opcode, true, true);
            else
                toBeRemoved.clearData(opcode, true);
        }
        gpuObjects.removeAll(unlockedGPUObjects);
    }
}