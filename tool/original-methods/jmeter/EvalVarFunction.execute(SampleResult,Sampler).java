/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String variableName = ((CompoundVariable) values[0]).execute();
    final JMeterVariables vars = getVariables();
    if (vars == null) {
        log.error("Variables have not yet been defined");
        return "**ERROR - see log file**";
    }
    String variableValue = vars.get(variableName);
    CompoundVariable cv = new CompoundVariable(variableValue);
    return cv.execute();
}