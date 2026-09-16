/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterVariables vars = getVariables();
    boolean perThread = isPerThread();
    String varName = getVarName();
    String counterString = getCounterString(perThread);
    if (vars != null && !varName.isEmpty()) {
        vars.put(varName, counterString);
    }
    return counterString;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isPerThread() throws InvalidVariableException {
    return Boolean.parseBoolean(((CompoundVariable) variables[0]).execute());
}

private String getVarName() throws InvalidVariableException {
    if (variables.length >= 2) {
        return ((CompoundVariable) variables[1]).execute().trim();
    }
    //$NON-NLS-1$
    return "";
}

private String getCounterString(boolean perThread) {
    if (perThread) {
        return String.valueOf(perThreadInt.get().addAndGet(1));
    } else {
        return String.valueOf(globalCounter.addAndGet(1));
    }
}

