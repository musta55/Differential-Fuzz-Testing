/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    int length = parseLength();
    String charsToUse = getCharsToUse();
    String myName = getParameterName();
    String myValue = generateRandomString(length, charsToUse);
    storeValueInVariables(myName, myValue);
    if (log.isDebugEnabled()) {
        //$NON-NLS-1$
        log.debug("{} name:{} value:{}", Thread.currentThread().getName(), myName, myValue);
    }
    return myValue;
}
// ---- helper method(s) introduced by the refactoring ----
private int parseLength() throws InvalidVariableException {
    return Integer.parseInt(values[0].execute());
}

private String getCharsToUse() throws InvalidVariableException {
    String charsToUse = null;
    if (values.length >= CHARS) {
        charsToUse = values[CHARS - 1].execute().trim();
        if (charsToUse.length() <= 0) {
            charsToUse = null;
        }
    }
    return charsToUse;
}

private String getParameterName() throws InvalidVariableException {
    String myName = "";
    if (values.length >= PARAM_NAME) {
        myName = values[PARAM_NAME - 1].execute().trim();
    }
    return myName;
}

private static String generateRandomString(int length, String charsToUse) {
    if (StringUtils.isEmpty(charsToUse)) {
        return RandomStringUtils.random(length);
    } else {
        return RandomStringUtils.random(length, charsToUse);
    }
}

private void storeValueInVariables(String myName, String myValue) {
    if (myName.length() > 0) {
        JMeterVariables vars = getVariables();
        if (vars != null) {
            vars.put(myName, myValue);
        }
    }
}

