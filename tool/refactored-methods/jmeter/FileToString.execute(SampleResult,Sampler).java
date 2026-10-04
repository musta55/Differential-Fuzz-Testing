/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String fileName = ((CompoundVariable) values[0]).execute();
    // means platform default
    Charset encoding = null;
    if (values.length >= ENCODING) {
        String encodingStr = ((CompoundVariable) values[ENCODING - 1]).execute().trim();
        if (!encodingStr.isEmpty()) {
            encoding = Charset.forName(encodingStr);
        }
    }
    // $NON-NLS-1$
    String varName = "";
    if (values.length >= PARAM_NAME) {
        varName = ((CompoundVariable) values[PARAM_NAME - 1]).execute().trim();
    }
    String result = ERR_IND;
    try {
        File file = new File(fileName);
        if (file.exists() && file.canRead()) {
            result = readFileToString(file, encoding);
        } else {
            log.warn("Could not read open: {}", fileName);
        }
    } catch (IOException e) {
        log.warn("Could not read file: {} {}", fileName, e.getMessage(), e);
    }
    if (!varName.isEmpty()) {
        JMeterVariables vars = getVariables();
        if (vars != null) {
            // Can be null if called from Config item testEnded() method
            vars.put(varName, result);
        }
    }
    if (log.isDebugEnabled()) {
        // $NON-NLS-1$
        log.debug("{} name: {} value: {}", Thread.currentThread().getName(), varName, result);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static String readFileToString(File file, Charset encoding) throws IOException {
    return FileUtils.readFileToString(file, encoding);
}

