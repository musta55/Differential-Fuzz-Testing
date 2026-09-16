/**
 * {@inheritDoc}
 */
@Override
public AssertionResult getResult(SampleResult response) {
    AssertionResult result = new AssertionResult(getName());
    final BeanShellInterpreter bshInterpreter = getBeanShellInterpreter();
    if (bshInterpreter == null) {
        result.setFailure(true);
        result.setError(true);
        result.setFailureMessage("BeanShell Interpreter not found");
        return result;
    }
    try {
        setVariablesInInterpreter(bshInterpreter, response);
        processFileOrScript(bshInterpreter);
        updateResultFromInterpreter(result, bshInterpreter);
    } catch (NoClassDefFoundError ex) {
        // NOSONAR explicitly trap this error to make tests work better
        log.error("BeanShell Jar missing?", ex);
        result.setError(true);
        result.setFailureMessage("BeanShell Jar missing? " + ex.toString());
        // No point continuing
        response.setStopThread(true);
    } catch (Exception ex) {
        // Mainly for bsh.EvalError
        result.setError(true);
        result.setFailureMessage(ex.toString());
        log.warn("Error in BeanShellAssertion", ex);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static void setVariablesInInterpreter(BeanShellInterpreter bshInterpreter, SampleResult response) throws Exception {
    //$NON-NLS-1$
    bshInterpreter.set("SampleResult", response);
    //$NON-NLS-1$
    bshInterpreter.set("Response", response);
    //$NON-NLS-1$
    bshInterpreter.set("ResponseData", response.getResponseData());
    //$NON-NLS-1$
    bshInterpreter.set("ResponseCode", response.getResponseCode());
    //$NON-NLS-1$
    bshInterpreter.set("ResponseMessage", response.getResponseMessage());
    //$NON-NLS-1$
    bshInterpreter.set("ResponseHeaders", response.getResponseHeaders());
    //$NON-NLS-1$
    bshInterpreter.set("RequestHeaders", response.getRequestHeaders());
    //$NON-NLS-1$
    bshInterpreter.set("SampleLabel", response.getSampleLabel());
    //$NON-NLS-1$
    bshInterpreter.set("SamplerData", response.getSamplerData());
    //$NON-NLS-1$
    bshInterpreter.set("Successful", response.isSuccessful());
    //$NON-NLS-1$ //$NON-NLS-2$
    bshInterpreter.set("FailureMessage", "");
    //$NON-NLS-1$
    bshInterpreter.set("Failure", false);
}

private static void updateResultFromInterpreter(AssertionResult result, BeanShellInterpreter bshInterpreter) throws Exception {
    //$NON-NLS-1$
    result.setFailureMessage(bshInterpreter.get("FailureMessage").toString());
    //$NON-NLS-1$
    result.setFailure(Boolean.parseBoolean(bshInterpreter.get("Failure").toString()));
    result.setError(false);
}

