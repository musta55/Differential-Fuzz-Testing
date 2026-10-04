/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String value = compute();
    handleVariableStorage(value);
    return value;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleVariableStorage(String value) throws InvalidVariableException {
    if (values.length >= 1) {
        // we have a variable name
        JMeterVariables vars = getVariables();
        if (vars != null) {
            // May be null if function is used on TestPlan
            String varName = ((CompoundVariable) values[0]).execute().trim();
            if (varName.length() > 0) {
                vars.put(varName, value);
            }
        }
    }
}

