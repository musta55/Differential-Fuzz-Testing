/* Implements BackendListenerClient.setupTest(BackendListenerContext) */
@Override
public void setupTest(BackendListenerContext context) throws Exception {
    if (log.isDebugEnabled()) {
        log.debug("{}: setupTest", getClass().getName());
    }
    metricsPerSampler.clear();
    userMetrics.clear();
}