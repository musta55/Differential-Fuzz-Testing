/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    boolean executionResult;
    try {
        executionResult = writeToFile();
    } catch (UnsupportedCharsetException ue) {
        // NOSONAR
        executionResult = false;
        log.error("The encoding of file is not supported", ue);
    } catch (IllegalCharsetNameException ie) {
        // NOSONAR
        executionResult = false;
        log.error("The encoding of file contains illegal characters", ie);
    } catch (IOException e) {
        // NOSONAR
        executionResult = false;
        log.error("IOException occurred", e);
    }
    return String.valueOf(executionResult);
}
// ---- helper method(s) introduced by the refactoring ----
private String getFileName() {
    return ((CompoundVariable) values[0]).execute().trim();
}

private String getContent() {
    String content = ((CompoundVariable) values[1]).execute();
    return NEW_LINE_PATTERN.matcher(content).replaceAll(System.lineSeparator());
}

private boolean getAppendFlag() {
    if (values.length >= 3) {
        String appendString = ((CompoundVariable) values[2]).execute().toLowerCase(Locale.ROOT).trim();
        return !appendString.isEmpty() && Boolean.parseBoolean(appendString);
    }
    return true;
}

private Charset getFileEncoding() {
    if (values.length == 4) {
        String charsetParamValue = ((CompoundVariable) values[3]).execute();
        if (StringUtils.isNotEmpty(charsetParamValue)) {
            return Charset.forName(charsetParamValue);
        }
    }
    return StandardCharsets.UTF_8;
}

