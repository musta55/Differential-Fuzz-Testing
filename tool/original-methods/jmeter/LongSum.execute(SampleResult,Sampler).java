/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterVariables vars = getVariables();
    long sum = 0;
    String varName = ((CompoundVariable) values[values.length - 1]).execute().trim();
    for (int i = 0; i < values.length - 1; i++) {
        sum += Long.parseLong(((CompoundVariable) values[i]).execute());
    }
    try {
        // Has chances to be a var
        sum += Long.parseLong(varName);
        // there is no variable name
        varName = null;
    } catch (NumberFormatException ignored) {
        // varName keeps its value and sum has not taken
        // into account non numeric or overflowing number
    }
    String totalString = Long.toString(sum);
    if (vars != null && varName != null && varName.length() > 0) {
        // vars will be null on TestPlan
        vars.put(varName, totalString);
    }
    return totalString;
}