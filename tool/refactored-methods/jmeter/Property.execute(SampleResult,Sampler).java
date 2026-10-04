/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String propertyName = getPropertyName();
    String propertyDefault = getPropertyDefault(propertyName);
    String propertyValue = JMeterUtils.getPropDefault(propertyName, propertyDefault);
    storePropertyValue(propertyValue);
    return propertyValue;
}
// ---- helper method(s) introduced by the refactoring ----
private String getPropertyName() throws InvalidVariableException {
    return ((CompoundVariable) values[0]).execute();
}

private String getPropertyDefault(String propertyName) throws InvalidVariableException {
    if (values.length > 2) {
        return ((CompoundVariable) values[2]).execute();
    }
    return propertyName;
}

private void storePropertyValue(String propertyValue) throws InvalidVariableException {
    if (values.length > 1) {
        String variableName = ((CompoundVariable) values[1]).execute();
        if (!variableName.isEmpty()) {
            final JMeterVariables variables = getVariables();
            if (variables != null) {
                variables.put(variableName, propertyValue);
            }
        }
    }
}

