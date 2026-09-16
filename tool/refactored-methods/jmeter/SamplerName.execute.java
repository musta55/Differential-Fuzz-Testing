/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String name = getCurrentSamplerName(currentSampler);
    handleVariableStorage(name);
    return name;
}
// ---- helper method(s) introduced by the refactoring ----
private static String getCurrentSamplerName(Sampler currentSampler) {
    return currentSampler != null ? currentSampler.getName() : "";
}

private void handleVariableStorage(String name) throws InvalidVariableException {
    if (values.length > 0) {
        JMeterVariables vars = getVariables();
        if (vars != null) {
            String varName = ((CompoundVariable) values[0]).execute().trim();
            if (!varName.isEmpty()) {
                vars.put(varName, name);
            }
        }
    }
}

