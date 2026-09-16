/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    String variablesNamesSplitBySeparatorValue = variablesNamesSplitBySeparator.execute().trim();
    JMeterVariables vars = getVariables();
    String outputValue = "";
    String separator = "";
    if (vars != null) {
        // vars will be null on TestPlan
        List<String> results = new ArrayList<>();
        String[] variables = variablesNamesSplitBySeparatorValue.split(SEPARATOR);
        for (String currentVarName : variables) {
            if (!StringUtils.isEmpty(currentVarName)) {
                extractVariableValuesToList(currentVarName, vars, results);
            }
        }
        if (!results.isEmpty()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(0, results.size());
            outputValue = results.get(randomIndex);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("RandomFromMultiResult didn't find <var>_matchNr in variables :'{}' using separator:'{}', will return empty value", variablesNamesSplitBySeparatorValue, separator);
            }
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