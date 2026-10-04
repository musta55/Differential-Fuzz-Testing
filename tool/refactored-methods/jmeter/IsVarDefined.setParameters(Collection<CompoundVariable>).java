@Override
public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
    checkParameterCount(parameters, MIN_PARAMETER_COUNT, MAX_PARAMETER_COUNT);
    values = parameters.toArray(new CompoundVariable[0]);
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isVariableDefined(JMeterVariables jMeterVariables, String variableName) {
    return jMeterVariables != null && jMeterVariables.get(variableName) != null;
}

