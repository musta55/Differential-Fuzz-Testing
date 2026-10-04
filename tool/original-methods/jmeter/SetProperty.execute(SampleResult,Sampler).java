/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String propertyName = ((CompoundVariable) values[0]).execute();
    String propertyValue = ((CompoundVariable) values[1]).execute();
    // should we return original value?
    boolean returnValue = false;
    if (values.length > 2) {
        //$NON-NLS-1$
        returnValue = ((CompoundVariable) values[2]).execute().equalsIgnoreCase("true");
    }
    if (returnValue) {
        // Only obtain and cast the return if needed
        return (String) JMeterUtils.setProperty(propertyName, propertyValue);
    } else {
        JMeterUtils.setProperty(propertyName, propertyValue);
        return "";
    }
}