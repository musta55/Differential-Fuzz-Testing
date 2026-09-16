/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String variablesNamesSplitBySeparatorValue = variablesNamesSplitBySeparator.execute().trim();
    JMeterVariables vars = getVariables();
    String outputValue = "";
    if (vars != null) {
        // vars will be null on TestPlan
        List<String> results = extractResults(vars, variablesNamesSplitBySeparatorValue);
        if (!results.isEmpty()) {
            outputValue = getRandomElement(results);
        } else {
            logDebugIfEnabled(variablesNamesSplitBySeparatorValue);
        }
        if (varName != null) {
            final String varTrim = varName.execute().trim();
            if (!varTrim.isEmpty()) {
                vars.put(varTrim, outputValue);
            }
        }
    }
    return outputValue;
}
// ---- helper method(s) introduced by the refactoring ----
private static List<String> extractResults(JMeterVariables vars, String variablesNamesSplitBySeparatorValue) {
    List<String> results = new ArrayList<>();
    String[] variables = variablesNamesSplitBySeparatorValue.split(SEPARATOR);
    for (String currentVarName : variables) {
        if (!StringUtils.isEmpty(currentVarName)) {
            extractVariableValuesToList(currentVarName, vars, results);
        }
    }
    return results;
}

private static String getRandomElement(List<String> results) {
    int randomIndex = ThreadLocalRandom.current().nextInt(0, results.size());
    return results.get(randomIndex);
}

private static void logDebugIfEnabled(String variablesNamesSplitBySeparatorValue) {
    if (log.isDebugEnabled()) {
        log.debug("RandomFromMultiResult didn't find <var>_matchNr in variables :'{}' using separator:'{}', will return empty value", variablesNamesSplitBySeparatorValue, SEPARATOR);
    }
}

