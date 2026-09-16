/**
 * @param previousResult {@link SampleResult}
 * @param currentSampler {@link Sampler}
 * @param jmctx {@link JMeterContext}
 * @param vars {@link JMeterVariables}
 * @param script Javascript code
 * @param varName variable name
 * @return result as String
 * @throws InvalidVariableException
 */
private static String executeWithNashorn(SampleResult previousResult, Sampler currentSampler, JMeterContext jmctx, JMeterVariables vars, String script, String varName) throws InvalidVariableException {
    String resultStr = null;
    try {
        ScriptContext newContext = new SimpleScriptContext();
        ScriptEngine engine = getInstance().getEngineByName(JavaScript.NASHORN_ENGINE_NAME);
        Bindings bindings = engine.createBindings();
        // Set up some objects for the script to play with
        //$NON-NLS-1$
        bindings.put("log", log);
        //$NON-NLS-1$
        bindings.put("ctx", jmctx);
        //$NON-NLS-1$
        bindings.put("vars", vars);
        //$NON-NLS-1$
        bindings.put("props", JMeterUtils.getJMeterProperties());
        //$NON-NLS-1$
        bindings.put("threadName", Thread.currentThread().getName());
        //$NON-NLS-1$
        bindings.put("sampler", currentSampler);
        //$NON-NLS-1$
        bindings.put("sampleResult", previousResult);
        newContext.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        Object result = engine.eval(script, newContext);
        resultStr = result.toString();
        if (varName != null && vars != null) {
            // vars can be null if run from TestPlan
            vars.put(varName, resultStr);
        }
    } catch (Exception e) {
        log.error("Error processing Javascript: [{}]", script, e);
        throw new InvalidVariableException("Error processing Javascript: [" + script + "]", e);
    }
    return resultStr;
}