/**
 * {@inheritDoc}
 */
@Override
public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
    checkParameterCount(parameters, 0, 2);
    Object[] values = parameters.toArray();
    int count = values.length;
    if (count > 0) {
        format = ((CompoundVariable) values[0]).execute();
    }
    if (count > 1) {
        variable = ((CompoundVariable) values[1]).execute().trim();
    }
}