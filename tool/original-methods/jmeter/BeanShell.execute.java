/**
 * {@inheritDoc}
 */
@Override
public synchronized String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    if (// did we find BeanShell?
    bshInterpreter == null) {
        throw new InvalidVariableException("BeanShell not found");
    }
    JMeterContext jmctx = JMeterContextService.getContext();
    JMeterVariables vars = jmctx.getVariables();
    String script = ((CompoundVariable) values[0]).execute();
    //$NON-NLS-1$
    String varName = "";
    if (values.length > 1) {
        varName = ((CompoundVariable) values[1]).execute().trim();
    }
    //$NON-NLS-1$
    String resultStr = "";
    try {
        // Pass in some variables
        if (currentSampler != null) {
            //$NON-NLS-1$
            bshInterpreter.set("Sampler", currentSampler);
        }
        if (previousResult != null) {
            //$NON-NLS-1$
            bshInterpreter.set("SampleResult", previousResult);
        }
        // Allow access to context and variables directly
        //$NON-NLS-1$
        bshInterpreter.set("ctx", jmctx);
        //$NON-NLS-1$
        bshInterpreter.set("vars", vars);
        //$NON-NLS-1$
        bshInterpreter.set("props", JMeterUtils.getJMeterProperties());
        //$NON-NLS-1$
        bshInterpreter.set("threadName", Thread.currentThread().getName());
        // Execute the script
        Object bshOut = bshInterpreter.eval(script);
        if (bshOut != null) {
            resultStr = bshOut.toString();
        }
        if (vars != null && varName.length() > 0) {
            // vars will be null on TestPlan
            vars.put(varName, resultStr);
        }
    } catch (// Mainly for bsh.EvalError
    Exception ex) {
        log.warn("Error running BSH script", ex);
    }
    log.debug("__Beanshell({},{})={}", script, varName, resultStr);
    return resultStr;
}