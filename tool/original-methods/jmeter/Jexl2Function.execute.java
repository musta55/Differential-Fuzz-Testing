/**
 * {@inheritDoc}
 */
@Override
public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
    //$NON-NLS-1$
    String str = "";
    CompoundVariable var = (CompoundVariable) values[0];
    String exp = var.execute();
    //$NON-NLS-1$
    String varName = "";
    if (values.length > 1) {
        varName = ((CompoundVariable) values[1]).execute().trim();
    }
    JMeterContext jmctx = JMeterContextService.getContext();
    JMeterVariables vars = jmctx.getVariables();
    try {
        JexlContext jc = new MapContext();
        //$NON-NLS-1$
        jc.set("log", log);
        //$NON-NLS-1$
        jc.set("ctx", jmctx);
        //$NON-NLS-1$
        jc.set("vars", vars);
        //$NON-NLS-1$
        jc.set("props", JMeterUtils.getJMeterProperties());
        // Previously mis-spelt as theadName
        //$NON-NLS-1$
        jc.set("threadName", Thread.currentThread().getName());
        //$NON-NLS-1$ (may be null)
        jc.set("sampler", currentSampler);
        //$NON-NLS-1$ (may be null)
        jc.set("sampleResult", previousResult);
        //$NON-NLS-1$
        jc.set("OUT", System.out);
        // Now evaluate the script, getting the result
        Script e = getJexlEngine().createScript(exp);
        Object o = e.execute(jc);
        if (o != null) {
            str = o.toString();
        }
        if (vars != null && varName.length() > 0) {
            // vars will be null on TestPlan
            vars.put(varName, str);
        }
    } catch (Exception e) {
        log.error("An error occurred while evaluating the expression \"" + exp + "\"\n", e);
    }
    return str;
}