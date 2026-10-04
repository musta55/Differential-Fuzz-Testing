/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String variableName = ((CompoundVariable) values[0]).execute();
    String variableDefault = getDefaultValue(variableName);
    String variableValue = getVariables().get(variableName);
    return variableValue == null ? variableDefault : variableValue;
}
// ---- helper method(s) introduced by the refactoring ----
private String getDefaultValue(String variableName) {
    if (values.length > 1) {
        return ((CompoundVariable) values[1]).execute();
    }
    return variableName;
}

