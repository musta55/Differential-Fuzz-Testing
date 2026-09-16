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
    logDetails(log, stringToLog, priorityString, t, "");
    return "";
}
// ---- helper method(s) introduced by the refactoring ----
private static void logDetails(Logger logger, String message, String level, Throwable throwable, String extraInfo) {
    switch(level.toUpperCase(Locale.ROOT)) {
        case "DEBUG":
            logger.debug(message, throwable);
            break;
        case "INFO":
            logger.info(message, throwable);
            break;
        case "WARN":
            logger.warn(message, throwable);
            break;
        case "ERROR":
            logger.error(message, throwable);
            break;
        case "OUT":
            System.out.println(message + extraInfo);
            break;
        case "ERR":
            System.err.println(message + extraInfo);
            break;
        default:
            logger.debug(message, throwable);
            break;
    }
}

