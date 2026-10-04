/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String fileName = ((CompoundVariable) values[0]).execute();
    //means platform default
    String encoding = null;
    if (values.length >= ENCODING) {
        encoding = ((CompoundVariable) values[ENCODING - 1]).execute().trim();
        if (encoding.length() <= 0) {
            // empty encoding, return to platform default
            encoding = null;
        }
    }
    //$NON-NLS-1$
    String myName = "";
    if (values.length >= PARAM_NAME) {
        myName = ((CompoundVariable) values[PARAM_NAME - 1]).execute().trim();
    }
    String myValue = ERR_IND;
    try {
        File file = new File(fileName);
        if (file.exists() && file.canRead()) {
            myValue = FileUtils.readFileToString(new File(fileName), encoding);
        } else {
            log.warn("Could not read open: {} ", fileName);
        }
    } catch (IOException e) {
        log.warn("Could not read file: {} {}", fileName, e.getMessage(), e);
    }
    if (myName.length() > 0) {
        JMeterVariables vars = getVariables();
        if (vars != null) {
            // Can be null if called from Config item testEnded() method
            vars.put(myName, myValue);
        }
    }
    if (log.isDebugEnabled()) {
        //$NON-NLS-1$
        log.debug("{} name: {} value: {}", Thread.currentThread().getName(), myName, myValue);
    }
    return myValue;
}