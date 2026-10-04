@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String variableName = values[0].execute();
    JMeterVariables jMeterVariables = getVariables();
    if (jMeterVariables != null) {
        String variableValue = jMeterVariables.get(variableName);
        return Boolean.toString(variableValue != null);
    } else {
        return Boolean.FALSE.toString();
    }
}