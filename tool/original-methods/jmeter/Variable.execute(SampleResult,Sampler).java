/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String variableName = ((CompoundVariable) values[0]).execute();
    String variableDefault = variableName;
    if (values.length > 1) {
        variableDefault = ((CompoundVariable) values[1]).execute();
    }
    String variableValue = getVariables().get(variableName);
    return variableValue == null ? variableDefault : variableValue;
}