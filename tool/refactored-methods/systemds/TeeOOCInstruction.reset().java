public static void reset() {
    if (!refCtr.isEmpty()) {
        System.err.println("There are some dangling streams still in the cache: " + refCtr);
        scheduleDeletionForAllStreams();
        refCtr.clear();
    }
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

