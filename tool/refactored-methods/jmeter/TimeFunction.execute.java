/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String datetime = resolveDateTime(format);
    if (!variable.isEmpty()) {
        JMeterVariables vars = getVariables();
        if (vars != null) {
            // vars will be null on TestPlan
            vars.put(variable, datetime);
        }
    }
    return datetime;
}
// ---- helper method(s) introduced by the refactoring ----
private static String resolveDateTime(String format) {
    if (format.isEmpty()) {
        // Default to milliseconds
        return Long.toString(System.currentTimeMillis());
    }
    // Resolve any aliases
    String fmt = aliases.getOrDefault(format, format);
    return DATE_TIME_FORMATTER_CACHE.get(fmt).get();
}

