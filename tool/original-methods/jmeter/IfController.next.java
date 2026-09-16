/**
 * @see org.apache.jmeter.control.Controller#next()
 */
@Override
public Sampler next() {
    // We should only evaluate the condition if it is the first
    // time ( first "iteration" ) we are called.
    // For subsequent calls, we are inside the IfControllerGroup,
    // so then we just pass the control to the next item inside the if control
    boolean result = true;
    if (isEvaluateAll() || isFirst()) {
        result = isUseExpression() ? evaluateExpression(getCondition()) : evaluateCondition(getCondition());
    }
    if (result) {
        return super.next();
    }
    // If-test is false, need to re-initialize indexes
    try {
        initializeSubControllers();
        return nextIsNull();
    } catch (NextIsNullException e1) {
        return null;
    }
}