/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String stringToLog = ((CompoundVariable) values[0]).execute();
    String priorityString;
    if (values.length > 1) {
        // We have a default
        priorityString = ((CompoundVariable) values[1]).execute();
        if (priorityString.length() == 0) {
            priorityString = DEFAULT_PRIORITY;
        }
    } else {
        priorityString = DEFAULT_PRIORITY;
    }
    Throwable t = null;
    if (values.length > 2) {
        // Throwable wanted
        t = new Throwable(((CompoundVariable) values[2]).execute());
    }
    LogFunction.logDetails(log, stringToLog, priorityString, t, "");
    return "";
}