/* Implements BackendListenerClient.teardownTest(BackendListenerContext) */
@Override
public void teardownTest(BackendListenerContext context) throws Exception {
    if (log.isDebugEnabled()) {
        log.debug("{}: teardownTest", getClass().getName());
    }
    clearMetrics();
}
// ---- helper method(s) introduced by the refactoring ----
private void clearMetrics() {
    metricsPerSampler.clear();
    userMetrics.clear();
}

