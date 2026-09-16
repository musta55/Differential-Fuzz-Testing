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
private static String executeWithRhino(SampleResult previousResult, Sampler currentSampler, JMeterContext jmctx, JMeterVariables vars, String script, String varName) throws InvalidVariableException {
    Context cx = Context.enter();
    String resultStr = null;
    try {
        Scriptable scope = cx.initStandardObjects(null);
        // Set up some objects for the script to play with
        //$NON-NLS-1$
        scope.put("log", scope, log);
        //$NON-NLS-1$
        scope.put("ctx", scope, jmctx);
        //$NON-NLS-1$
        scope.put("vars", scope, vars);
        //$NON-NLS-1$
        scope.put("props", scope, JMeterUtils.getJMeterProperties());
        // Previously mis-spelt as theadName
        //$NON-NLS-1$
        scope.put("threadName", scope, Thread.currentThread().getName());
        //$NON-NLS-1$
        scope.put("sampler", scope, currentSampler);
        //$NON-NLS-1$
        scope.put("sampleResult", scope, previousResult);
        //$NON-NLS-1$
        Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);
        resultStr = Context.toString(result);
        if (varName != null && vars != null) {
            // vars can be null if run from TestPlan
            vars.put(varName, resultStr);
        }
    } catch (RhinoException e) {
        log.error("Error processing Javascript: [{}]", script, e);
        throw new InvalidVariableException("Error processing Javascript: [" + script + "]", e);
    } finally {
        Context.exit();
    }
    return resultStr;
}