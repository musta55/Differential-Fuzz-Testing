/* Implements BackendListenerClient.teardownTest(BackendListenerContext) */
@Override
public void teardownTest(BackendListenerContext context) throws Exception {
    if (log.isDebugEnabled()) {
        log.debug("{}: teardownTest", getClass().getName());
    }
    metricsPerSampler.clear();
    userMetrics.clear();
}