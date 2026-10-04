@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String variableName = values[0].execute();
    JMeterVariables jMeterVariables = getVariables();
    return Boolean.toString(isVariableDefined(jMeterVariables, variableName));
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isVariableDefined(JMeterVariables jMeterVariables, String variableName) {
    return jMeterVariables != null && jMeterVariables.get(variableName) != null;
}

