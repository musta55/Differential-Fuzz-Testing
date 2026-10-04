public void processInstruction(ExecutionContext ec) {
    // Get input stream
    MatrixObject min = ec.getMatrixObject(input1);
    OOCStreamable<IndexedMatrixValue> streamable = min.getStreamable();
    CachingStream handle;
    if (streamable.hasStreamCache()) {
        handle = streamable.getStreamCache();
        incrRef(handle, 1);
    } else {
        // We also set the input stream handle
        handle = new CachingStream(min.getStreamHandle());
        min.setStreamHandle(handle);
        incrRef(handle, 2);
    }
    // Get output and create new resettable stream
    MatrixObject mo = ec.getMatrixObject(output);
    setOutputStreamHandle(mo, handle, min);
}
// ---- helper method(s) introduced by the refactoring ----
private static void scheduleDeletionForAllStreams() {
    for (CachingStream cache : refCtr.keySet()) {
        try {
            cache.scheduleDeletion();
        } catch (Exception ex) {
            System.err.println("Failed to schedule deletion for dangling stream " + cache + ": " + ex.getMessage());
        }
    }
}

private static int updateReferenceCount(CachingStream cache, int incr) {
    return refCtr.compute(cache, (k, v) -> {
        if (v == null) {
            v = 0;
        }
        v += incr;
        return v <= 0 ? null : v;
    });
}

private void setOutputStreamHandle(MatrixObject mo, CachingStream handle, MatrixObject min) {
    mo.setStreamHandle(handle);
    mo.setMetaData(min.getMetaData());
}

