/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    if (values == null || values.length != 1) {
        throw new InvalidVariableException("Invalid number of parameters");
    }
    String variableName = ((CompoundVariable) values[0]).execute();
    final JMeterVariables vars = getVariables();
    if (vars == null) {
        log.error("Variables have not yet been defined");
        return "**ERROR - see log file**";
    }
    String variableValue = vars.get(variableName);
    if (variableValue == null) {
        log.error("Variable {} is not defined", variableName);
        return "**ERROR - see log file**";
    }
    CompoundVariable cv = new CompoundVariable(variableValue);
    return cv.execute();
}