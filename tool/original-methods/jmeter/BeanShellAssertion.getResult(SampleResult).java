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
        // Add SamplerData for consistency with BeanShell Sampler
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
        // The following are used to set the Result details on return from
        // the script:
        //$NON-NLS-1$ //$NON-NLS-2$
        bshInterpreter.set("FailureMessage", "");
        //$NON-NLS-1$
        bshInterpreter.set("Failure", false);
        processFileOrScript(bshInterpreter);
        //$NON-NLS-1$
        result.setFailureMessage(bshInterpreter.get("FailureMessage").toString());
        result.setFailure(Boolean.parseBoolean(//$NON-NLS-1$
        bshInterpreter.get("Failure").toString()));
        result.setError(false);
    } catch (NoClassDefFoundError ex) {
        // NOSONAR explicitly trap this error to make tests work better
        log.error("BeanShell Jar missing?", ex);
        result.setError(true);
        result.setFailureMessage("BeanShell Jar missing? " + ex.toString());
        // No point continuing
        response.setStopThread(true);
    } catch (// Mainly for bsh.EvalError
    Exception ex) {
        result.setError(true);
        result.setFailureMessage(ex.toString());
        log.warn("Error in BeanShellAssertion", ex);
    }
    return result;
}