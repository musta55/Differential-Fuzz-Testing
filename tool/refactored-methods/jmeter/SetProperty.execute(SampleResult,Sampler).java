/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String propertyName = ((CompoundVariable) values[0]).execute();
    String propertyValue = ((CompoundVariable) values[1]).execute();
    //$NON-NLS-1$
    boolean returnValue = values.length > 2 && ((CompoundVariable) values[2]).execute().equalsIgnoreCase("true");
    return setProperty(propertyName, propertyValue, returnValue);
}
// ---- helper method(s) introduced by the refactoring ----
private static String setProperty(String propertyName, String propertyValue, boolean returnValue) {
    if (returnValue) {
        return (String) JMeterUtils.setProperty(propertyName, propertyValue);
    } else {
        JMeterUtils.setProperty(propertyName, propertyValue);
        return "";
    }
}

