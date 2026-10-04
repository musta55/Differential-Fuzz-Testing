/**
 * {@inheritDoc}
 */
@Override
public synchronized String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    Bindings bindings = scriptEngine.createBindings();
    populateBindings(bindings);
    String script = ((CompoundVariable) values[0]).execute();
    //$NON-NLS-1$
    String varName = "";
    if (values.length > 1) {
        varName = ((CompoundVariable) values[1]).execute().trim();
    }
    //$NON-NLS-1$
    String resultStr = "";
    try {
        // Pass in some variables
        if (currentSampler != null) {
            // $NON-NLS-1$
            bindings.put("sampler", currentSampler);
        }
        if (previousResult != null) {
            //$NON-NLS-1$
            bindings.put("prev", previousResult);
        }
        // $NON-NLS-1$ (this name is fixed)
        bindings.put("log", log);
        // Add variables for access to context and variables
        bindings.put("threadName", Thread.currentThread().getName());
        JMeterContext jmctx = JMeterContextService.getContext();
        // $NON-NLS-1$ (this name is fixed)
        bindings.put("ctx", jmctx);
        JMeterVariables vars = jmctx.getVariables();
        // $NON-NLS-1$ (this name is fixed)
        bindings.put("vars", vars);
        Properties props = JMeterUtils.getJMeterProperties();
        // $NON-NLS-1$ (this name is fixed)
        bindings.put("props", props);
        // For use in debugging:
        // $NON-NLS-1$ (this name is fixed)
        bindings.put("OUT", System.out);
        // Execute the script
        Object out = scriptEngine.eval(script, bindings);
        if (out != null) {
            resultStr = out.toString();
        }
        if (varName.length() > 0 && vars != null) {
            // vars will be null on TestPlan
            vars.put(varName, resultStr);
        }
    } catch (// Mainly for bsh.EvalError
    Exception ex) {
        log.warn("Error running groovy script", ex);
    }
    log.debug("__groovy({},{})={}", script, varName, resultStr);
    return resultStr;
}