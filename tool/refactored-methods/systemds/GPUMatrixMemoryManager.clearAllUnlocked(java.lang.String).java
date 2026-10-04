/**
 * Clear all unlocked gpu objects that are not lineage cached
 *
 * @param opcode instruction code
 * @throws DMLRuntimeException if error
 */
void clearAllUnlocked(String opcode) throws DMLRuntimeException {
    Set<GPUObject> unlockedGPUObjects = filterUnlockedGPUObjects();
    if (!unlockedGPUObjects.isEmpty()) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Clearing all unlocked matrices (count=" + unlockedGPUObjects.size() + ").");
        }
        processUnlockedGPUObjects(unlockedGPUObjects, opcode);
        gpuObjects.removeAll(unlockedGPUObjects);
    }
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

