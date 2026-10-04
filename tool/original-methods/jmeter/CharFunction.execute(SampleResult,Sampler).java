/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    StringBuilder sb = new StringBuilder(values.length);
    for (Object val : values) {
        String numberString = ((CompoundVariable) val).execute().trim();
        try {
            long value = Long.decode(numberString);
            char ch = (char) value;
            sb.append(ch);
        } catch (NumberFormatException e) {
            log.warn("Could not parse {} : {}", numberString, e.toString());
        }
    }
    return sb.toString();
}