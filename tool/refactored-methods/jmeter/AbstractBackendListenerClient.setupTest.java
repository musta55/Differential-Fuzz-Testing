/* Implements BackendListenerClient.setupTest(BackendListenerContext) */
@Override
public void setupTest(BackendListenerContext context) throws Exception {
    if (log.isDebugEnabled()) {
        log.debug("{}: setupTest", getClass().getName());
    }
    clearMetrics();
}
// ---- helper method(s) introduced by the refactoring ----
private void clearMetrics() {
    metricsPerSampler.clear();
    userMetrics.clear();
}

