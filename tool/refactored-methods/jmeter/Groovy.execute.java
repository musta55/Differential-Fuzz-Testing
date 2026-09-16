/**
 * {@inheritDoc}
 */
@Override
public synchronized String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    Bindings bindings = scriptEngine.createBindings();
    addBindings(bindings, previousResult, currentSampler);
    String script = ((CompoundVariable) values[0]).execute();
    //$NON-NLS-1$
    String varName = "";
    if (values.length > 1) {
        varName = ((CompoundVariable) values[1]).execute().trim();
    }
    //$NON-NLS-1$
    String resultStr = "";
    try {
        // Execute the script
        Object out = scriptEngine.eval(script, bindings);
        if (out != null) {
            resultStr = out.toString();
        }
        if (!varName.isEmpty() && bindings.get("vars") != null) {
            // vars will be null on TestPlan
            ((JMeterVariables) bindings.get("vars")).put(varName, resultStr);
        }
    } catch (Exception ex) {
        // Mainly for bsh.EvalError
        log.warn("Error running groovy script", ex);
    }
    log.debug("__groovy({},{})={}", script, varName, resultStr);
    return resultStr;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Populate variables to be passed to scripts
 * @param bindings Bindings
 */
protected static void addBindings(Bindings bindings, SampleResult previousResult, Sampler currentSampler) {
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
}

private static File resolveFile(String fileName) throws InvalidVariableException {
    File file = new File(fileName);
    if (!(file.exists() && file.canRead())) {
        // File maybe relative to JMeter home
        file = new File(JMeterUtils.getJMeterHome(), fileName);
        if (!(file.exists() && file.canRead())) {
            throw new InvalidVariableException("Cannot read file, neither from:" + new File(fileName).getAbsolutePath() + ", nor from:" + file.getAbsolutePath() + ", check property '" + INIT_FILE + "'");
        }
    }
    return file;
}

