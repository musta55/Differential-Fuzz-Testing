@Override
public AssertionResult getResult(SampleResult response) {
    AssertionResult result = new AssertionResult(getName());
    BSFManager mgr = null;
    try {
        mgr = getManager();
        mgr.declareBean("SampleResult", response, SampleResult.class);
        mgr.declareBean("AssertionResult", result, AssertionResult.class);
        processFileOrScript(mgr);
        result.setError(false);
    } catch (BSFException e) {
        log.warn("Problem in BSF script", e);
        result.setFailure(true);
        result.setError(true);
        result.setFailureMessage(e.toString());
    } finally {
        if (mgr != null) {
            mgr.terminate();
        }
    }
    return result;
}