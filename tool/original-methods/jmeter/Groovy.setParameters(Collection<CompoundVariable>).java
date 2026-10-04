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
        File file = new File(fileName);
        if (!(file.exists() && file.canRead())) {
            // File maybe relative to JMeter home
            File oldFile = file;
            file = new File(JMeterUtils.getJMeterHome(), fileName);
            if (!(file.exists() && file.canRead())) {
                throw new InvalidVariableException("Cannot read file, neither from:" + oldFile.getAbsolutePath() + ", nor from:" + file.getAbsolutePath() + ", check property '" + INIT_FILE + "'");
            }
        }
        try (Reader reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset())) {
            Bindings bindings = scriptEngine.createBindings();
            bindings.put("log", log);
            scriptEngine.eval(reader, bindings);
        } catch (Exception ex) {
            throw new InvalidVariableException("Failed loading script:" + file.getAbsolutePath(), ex);
        }
    }
}