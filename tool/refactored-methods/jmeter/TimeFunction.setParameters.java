/**
 * {@inheritDoc}
 */
@Override
public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
    checkParameterCount(parameters, 0, 2);
    Object[] values = parameters.toArray();
    if (values.length > 0) {
        format = ((CompoundVariable) values[0]).execute();
    }
    if (values.length > 1) {
        variable = ((CompoundVariable) values[1]).execute().trim();
    }
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

