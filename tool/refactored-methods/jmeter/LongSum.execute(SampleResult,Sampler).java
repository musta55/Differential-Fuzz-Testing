/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterVariables vars = getVariables();
    long sum = parseAndSumValues();
    String varName = getVariableName();
    if (vars != null && varName != null && !varName.isEmpty()) {
        vars.put(varName, Long.toString(sum));
    }
    return Long.toString(sum);
}
// ---- helper method(s) introduced by the refactoring ----
private long parseAndSumValues() {
    long sum = 0;
    for (int i = 0; i < values.length - 1; i++) {
        sum += Long.parseLong(((CompoundVariable) values[i]).execute());
    }
    String varName = ((CompoundVariable) values[values.length - 1]).execute().trim();
    try {
        sum += Long.parseLong(varName);
    } catch (NumberFormatException ignored) {
        // varName keeps its value and sum has not taken
        // into account non numeric or overflowing number
    }
    return sum;
}

private String getVariableName() {
    String varName = ((CompoundVariable) values[values.length - 1]).execute().trim();
    try {
        Long.parseLong(varName);
        // there is no variable name
        return null;
    } catch (NumberFormatException e) {
        return varName;
    }
}

