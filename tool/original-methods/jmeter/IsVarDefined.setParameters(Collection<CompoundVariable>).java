@Override
public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
    checkParameterCount(parameters, MIN_PARAMETER_COUNT, MAX_PARAMETER_COUNT);
    values = parameters.toArray(new CompoundVariable[parameters.size()]);
}