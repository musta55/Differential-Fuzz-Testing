/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    /*
         * boolean fullHostName = false; if (((CompoundFunction) values[0])
         * .execute() .toLowerCase() .equals("true")) { fullHostName = true; }
         */
    String value = compute();
    if (values.length >= 1) {
        // we have a variable name
        JMeterVariables vars = getVariables();
        if (vars != null) {
            // May be null if function is used on TestPlan
            String varName = ((CompoundVariable) values[0]).execute().trim();
            if (varName.length() > 0) {
                vars.put(varName, value);
            }
        }
    }
    return value;
}