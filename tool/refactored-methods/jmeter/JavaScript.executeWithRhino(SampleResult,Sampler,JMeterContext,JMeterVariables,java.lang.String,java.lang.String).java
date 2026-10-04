/**
 * @param previousResult {@link SampleResult}
 * @param currentSampler {@link Sampler}
 * @param jmctx {@link JMeterContext}
 * @param vars {@link JMeterVariables}
 * @param script Javascript code
 * @param varName variable name
 * @return result as String
 * @throws Exception
 */
private static String executeWithRhino(SampleResult previousResult, Sampler currentSampler, JMeterContext jmctx, JMeterVariables vars, String script, String varName) throws Exception {
    Context cx = Context.enter();
    String resultStr = null;
    try {
        Scriptable scope = cx.initStandardObjects(null);
        setupBindings(scope, jmctx, vars, currentSampler, previousResult);
        //$NON-NLS-1$
        Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);
        resultStr = Context.toString(result);
        storeResult(vars, varName, resultStr);
    } finally {
        Context.exit();
    }
    return resultStr;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * @param previousResult {@link SampleResult}
 * @param currentSampler {@link Sampler}
 * @param jmctx {@link JMeterContext}
 * @param vars {@link JMeterVariables}
 * @param script Javascript code
 * @param varName variable name
 * @param useRhino flag to indicate whether to use Rhino engine
 * @return result as String
 * @throws InvalidVariableException
 */
private static String executeWithEngine(SampleResult previousResult, Sampler currentSampler, JMeterContext jmctx, JMeterVariables vars, String script, String varName, boolean useRhino) throws InvalidVariableException {
    String resultStr = null;
    try {
        if (useRhino) {
            resultStr = executeWithRhino(previousResult, currentSampler, jmctx, vars, script, varName);
        } else {
            resultStr = executeWithNashorn(previousResult, currentSampler, jmctx, vars, script, varName);
        }
    } catch (Exception e) {
        log.error("Error processing Javascript: [{}]", script, e);
        throw new InvalidVariableException("Error processing Javascript: [" + script + "]", e);
    }
    return resultStr;
}

/**
 * Setup bindings for Nashorn and Rhino engines
 * @param bindings Bindings object
 * @param jmctx JMeterContext
 * @param vars JMeterVariables
 * @param currentSampler Sampler
 * @param previousResult SampleResult
 */
private static void setupBindings(Object bindings, JMeterContext jmctx, JMeterVariables vars, Sampler currentSampler, SampleResult previousResult) {
    if (bindings instanceof Bindings) {
        Bindings b = (Bindings) bindings;
        //$NON-NLS-1$
        b.put("log", log);
        //$NON-NLS-1$
        b.put("ctx", jmctx);
        //$NON-NLS-1$
        b.put("vars", vars);
        //$NON-NLS-1$
        b.put("props", JMeterUtils.getJMeterProperties());
        //$NON-NLS-1$
        b.put("threadName", Thread.currentThread().getName());
        //$NON-NLS-1$
        b.put("sampler", currentSampler);
        //$NON-NLS-1$
        b.put("sampleResult", previousResult);
    } else if (bindings instanceof Scriptable) {
        Scriptable s = (Scriptable) bindings;
        //$NON-NLS-1$
        s.put("log", s, log);
        //$NON-NLS-1$
        s.put("ctx", s, jmctx);
        //$NON-NLS-1$
        s.put("vars", s, vars);
        //$NON-NLS-1$
        s.put("props", s, JMeterUtils.getJMeterProperties());
        //$NON-NLS-1$
        s.put("threadName", s, Thread.currentThread().getName());
        //$NON-NLS-1$
        s.put("sampler", s, currentSampler);
        //$NON-NLS-1$
        s.put("sampleResult", s, previousResult);
    }
}

/**
 * Store result in JMeterVariables if varName is not null
 * @param vars JMeterVariables
 * @param varName variable name
 * @param resultStr result as String
 */
private static void storeResult(JMeterVariables vars, String varName, String resultStr) {
    if (varName != null && vars != null) {
        // vars can be null if run from TestPlan
        vars.put(varName, resultStr);
    }
}

