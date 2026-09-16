/**
 * @see org.apache.jmeter.control.Controller#next()
 */
@Override
public Sampler next() {
    boolean result = evaluateCurrentCondition();
    if (result) {
        return super.next();
    }
    resetAndReturnNull();
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * @param condition
 * @param result
 * @return boolean
 * @throws Exception
 */
private static boolean computeBooleanResult(String condition, Object result) throws Exception {
    String resultStr = result.toString();
    switch(resultStr) {
        case "false":
            return false;
        case "true":
            return true;
        default:
            throw new Exception(" BAD CONDITION :: " + condition + " :: expected true or false");
    }
}

private boolean evaluateCurrentCondition() {
    if (isEvaluateAll() || isFirst()) {
        return isUseExpression() ? evaluateExpression(getCondition()) : evaluateCondition(getCondition());
    }
    return true;
}

private void resetAndReturnNull() {
    try {
        initializeSubControllers();
        nextIsNull();
    } catch (NextIsNullException e1) {
        log.warn("Next is null exception caught", e1);
    }
}

