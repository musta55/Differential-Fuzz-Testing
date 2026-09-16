/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterVariables vars = getVariables();
    String varName = getVarName();
    int sum = calculateSum();
    try {
        sum += Integer.parseInt(varName);
        // there is no variable name
        varName = null;
    } catch (NumberFormatException ignored) {
        // varName keeps its value and sum has not taken
        // into account non numeric or overflowing number
    }
    String totalString = Integer.toString(sum);
    if (vars != null && varName != null) {
        // vars will be null on TestPlan
        vars.put(varName.trim(), totalString);
    }
    return totalString;
}
// ---- helper method(s) introduced by the refactoring ----
private String getVarName() {
    // trim() see bug 55871
    return ((CompoundVariable) values[values.length - 1]).execute().trim();
}

private int calculateSum() {
    int sum = 0;
    for (int i = 0; i < values.length - 1; i++) {
        sum += Integer.parseInt(((CompoundVariable) values[i]).execute());
    }
    return sum;
}

