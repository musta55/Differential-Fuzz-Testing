/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String propertyName = ((CompoundVariable) values[0]).execute();
    String propertyDefault = propertyName;
    if (values.length > 2) {
        // We have a 3rd parameter
        propertyDefault = ((CompoundVariable) values[2]).execute();
    }
    String propertyValue = JMeterUtils.getPropDefault(propertyName, propertyDefault);
    if (values.length > 1) {
        String variableName = ((CompoundVariable) values[1]).execute();
        if (variableName.length() > 0) {
            // Allow for empty name
            final JMeterVariables variables = getVariables();
            if (variables != null) {
                variables.put(variableName, propertyValue);
            }
        }
    }
    return propertyValue;
}