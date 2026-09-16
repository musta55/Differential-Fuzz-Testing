/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String datetime;
    if (format.isEmpty()) {
        // Default to milliseconds
        datetime = Long.toString(System.currentTimeMillis());
    } else {
        // Resolve any aliases
        String fmt = aliases.get(format);
        if (fmt == null) {
            // Not found
            fmt = format;
        }
        datetime = DATE_TIME_FORMATTER_CACHE.get(fmt).get();
    }
    if (!variable.isEmpty()) {
        JMeterVariables vars = getVariables();
        if (vars != null) {
            // vars will be null on TestPlan
            vars.put(variable, datetime);
        }
    }
    return datetime;
}