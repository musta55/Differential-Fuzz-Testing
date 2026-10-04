/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterVariables vars = getVariables();
    boolean perThread = Boolean.parseBoolean(((CompoundVariable) variables[0]).execute());
    //$NON-NLS-1$
    String varName = "";
    if (variables.length >= 2) {
        // Ensure variable has been provided
        varName = ((CompoundVariable) variables[1]).execute().trim();
    }
    //$NON-NLS-1$
    String counterString = "";
    if (perThread) {
        counterString = String.valueOf(perThreadInt.get().addAndGet(1));
    } else {
        counterString = String.valueOf(globalCounter.addAndGet(1));
    }
    // vars will be null on Test Plan
    if (vars != null && varName.length() > 0) {
        vars.put(varName, counterString);
    }
    return counterString;
}