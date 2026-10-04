/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    int length = Integer.parseInt(values[0].execute());
    //means no restriction
    String charsToUse = null;
    if (values.length >= CHARS) {
        charsToUse = values[CHARS - 1].execute().trim();
        if (charsToUse.length() <= 0) {
            // empty chars, return to null
            charsToUse = null;
        }
    }
    //$NON-NLS-1$
    String myName = "";
    if (values.length >= PARAM_NAME) {
        myName = values[PARAM_NAME - 1].execute().trim();
    }
    String myValue = null;
    if (StringUtils.isEmpty(charsToUse)) {
        myValue = RandomStringUtils.random(length);
    } else {
        myValue = RandomStringUtils.random(length, charsToUse);
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
        log.debug("{} name:{} value:{}", Thread.currentThread().getName(), myName, myValue);
    }
    return myValue;
}