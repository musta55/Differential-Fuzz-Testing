/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    StringBuilder sb = new StringBuilder(values.length);
    for (Object val : values) {
        String numberString = ((CompoundVariable) val).execute().trim();
        try {
            char ch = parseToChar(numberString);
            sb.append(ch);
        } catch (NumberFormatException e) {
            log.warn("Could not parse {} : {}", numberString, e.toString());
        }
    }
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static char parseToChar(String numberString) throws NumberFormatException {
    long value = Long.decode(numberString);
    return (char) value;
}

