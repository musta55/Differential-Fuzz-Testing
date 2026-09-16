/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterVariables vars = getVariables();
    String stringToSplit = ((CompoundVariable) values[0]).execute();
    String varNamePrefix = ((CompoundVariable) values[1]).execute().trim();
    String splitString = getSplitString();
    log.debug("Split {} using {} into {}", stringToSplit, splitString, varNamePrefix);
    // $NON-NLS-1$
    String[] parts = JOrphanUtils.split(stringToSplit, splitString, "?");
    setVariables(vars, varNamePrefix, parts);
    return stringToSplit;
}
// ---- helper method(s) introduced by the refactoring ----
private String getSplitString() {
    String splitString = ",";
    if (values.length > 2) {
        // Split string provided
        String newSplitString = ((CompoundVariable) values[2]).execute();
        splitString = newSplitString.length() > 0 ? newSplitString : splitString;
    }
    return splitString;
}

private static void setVariables(JMeterVariables vars, String varNamePrefix, String[] parts) {
    vars.put(varNamePrefix, String.join(",", parts));
    // $NON-NLS-1$
    vars.put(varNamePrefix + "_n", Integer.toString(parts.length));
    for (int i = 1; i <= parts.length; i++) {
        if (log.isDebugEnabled()) {
            log.debug(parts[i - 1]);
        }
        // $NON-NLS-1$
        vars.put(varNamePrefix + "_" + i, parts[i - 1]);
    }
    vars.remove(varNamePrefix + "_" + (parts.length + 1));
}

