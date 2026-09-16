/**
 * {@inheritDoc}
 */
@Override
public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
    checkParameterCount(parameters, 1, 2);
    values = parameters.toArray();
    //$NON-NLS-N$
    scriptEngine = JSR223TestElement.getInstance().getEngineByName(GROOVY_ENGINE_NAME);
    String fileName = JMeterUtils.getProperty(INIT_FILE);
    if (!StringUtils.isEmpty(fileName)) {
        File file = resolveFile(fileName);
        try (Reader reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset())) {
            Bindings bindings = scriptEngine.createBindings();
            bindings.put("log", log);
            scriptEngine.eval(reader, bindings);
        } catch (Exception ex) {
            throw new InvalidVariableException("Failed loading script:" + file.getAbsolutePath(), ex);
        }
    }
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

