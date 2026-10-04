/**
 * Returns the result of the Assertion. Here it checks whether the Sample
 * took to long to be considered successful. If so an AssertionResult
 * containing a FailureMessage will be returned. Otherwise the returned
 * AssertionResult will reflect the success of the Sample.
 */
@Override
public AssertionResult getResult(SampleResult response) {
    AssertionResult result = new AssertionResult(getName());
    result.setFailure(false);
    long duration = getAllowedDuration();
    if (duration > 0) {
        long responseTime = response.getTime();
        // has the Sample lasted too long?
        if (responseTime > duration) {
            setFailureAndMessage(result, responseTime, duration);
        }
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Sets the failure status and message for the AssertionResult.
 */
private static void setFailureAndMessage(AssertionResult result, long responseTime, long duration) {
    result.setFailure(true);
    Object[] arguments = { responseTime, duration };
    String message = MessageFormat.format(// $NON-NLS-1$
    JMeterUtils.getResString("duration_assertion_failure"), arguments);
    result.setFailureMessage(message);
}

