/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterVariables vars = getVariables();
    String stringToSplit = ((CompoundVariable) values[0]).execute();
    String varNamePrefix = ((CompoundVariable) values[1]).execute().trim();
    String splitString = ",";
    if (values.length > 2) {
        // Split string provided
        String newSplitString = ((CompoundVariable) values[2]).execute();
        splitString = newSplitString.length() > 0 ? newSplitString : splitString;
    }
    log.debug("Split {} using {} into {}", stringToSplit, splitString, varNamePrefix);
    // $NON-NLS-1$
    String[] parts = JOrphanUtils.split(stringToSplit, splitString, "?");
    vars.put(varNamePrefix, stringToSplit);
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
    return stringToSplit;
}