/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    JMeterContext jmctx = JMeterContextService.getContext();
    JMeterVariables vars = jmctx.getVariables();
    String script = ((CompoundVariable) values[0]).execute();
    // Allow variable to be omitted
    String varName = values.length < 2 ? null : ((CompoundVariable) values[1]).execute().trim();
    String resultStr = "";
    if (useRhinoEngine) {
        resultStr = executeWithRhino(previousResult, currentSampler, jmctx, vars, script, varName);
    } else {
        resultStr = executeWithNashorn(previousResult, currentSampler, jmctx, vars, script, varName);
    }
    return resultStr;
}