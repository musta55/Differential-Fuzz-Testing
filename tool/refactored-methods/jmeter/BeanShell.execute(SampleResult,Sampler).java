/**
 * {@inheritDoc}
 */
@Override
public synchronized String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    if (bshInterpreter == null) {
        throw new InvalidVariableException("BeanShell not found");
    }
    JMeterContext jmctx = JMeterContextService.getContext();
    JMeterVariables vars = jmctx.getVariables();
    String script = ((CompoundVariable) values[0]).execute();
    String varName = getVarName();
    String resultStr = "";
    try {
        setScriptVariables(currentSampler, previousResult, jmctx, vars);
        Object bshOut = bshInterpreter.eval(script);
        if (bshOut != null) {
            resultStr = bshOut.toString();
        }
        if (vars != null && !varName.isEmpty()) {
            vars.put(varName, resultStr);
        }
    } catch (Exception ex) {
        log.warn("Error running BSH script", ex);
    }
    log.debug("__Beanshell({},{})={}", script, varName, resultStr);
    return resultStr;
}
// ---- helper method(s) introduced by the refactoring ----
private String getVarName() {
    if (values.length > 1) {
        return ((CompoundVariable) values[1]).execute().trim();
    }
    //$NON-NLS-1$
    return "";
}

private void setScriptVariables(Sampler currentSampler, SampleResult previousResult, JMeterContext jmctx, JMeterVariables vars) throws Exception {
    if (currentSampler != null) {
        //$NON-NLS-1$
        bshInterpreter.set("Sampler", currentSampler);
    }
    if (previousResult != null) {
        //$NON-NLS-1$
        bshInterpreter.set("SampleResult", previousResult);
    }
    //$NON-NLS-1$
    bshInterpreter.set("ctx", jmctx);
    //$NON-NLS-1$
    bshInterpreter.set("vars", vars);
    //$NON-NLS-1$
    bshInterpreter.set("props", JMeterUtils.getJMeterProperties());
    //$NON-NLS-1$
    bshInterpreter.set("threadName", Thread.currentThread().getName());
}

