@Override
public void process() {
    StringBuilder sb = new StringBuilder(100);
    // for request Data
    StringBuilder rd = new StringBuilder(20);
    SampleResult sr = new SampleResult();
    sr.setSampleLabel(getName());
    sr.sampleStart();
    JMeterContext threadContext = getThreadContext();
    appendIfEnabled(sb, rd, "SamplerProperties\n", "SamplerProperties:\n", isDisplaySamplerProperties(), () -> formatPropertyIterator(sb, threadContext.getCurrentSampler().propertyIterator()));
    appendIfEnabled(sb, rd, "JMeterVariables\n", "JMeterVariables:\n", isDisplayJMeterVariables(), () -> formatSet(sb, threadContext.getVariables().entrySet()));
    appendIfEnabled(sb, rd, "JMeterProperties\n", "JMeterProperties:\n", isDisplayJMeterProperties(), () -> formatSet(sb, JMeterUtils.getJMeterProperties().entrySet()));
    appendIfEnabled(sb, rd, "SystemProperties\n", "SystemProperties:\n", isDisplaySystemProperties(), () -> formatSet(sb, System.getProperties().entrySet()));
    sr.setThreadName(threadContext.getThread().getThreadName());
    sr.setGroupThreads(threadContext.getThreadGroup().getNumberOfThreads());
    sr.setAllThreads(JMeterContextService.getNumberOfThreads());
    sr.setResponseData(sb.toString(), null);
    sr.setDataType(SampleResult.TEXT);
    sr.setSamplerData(rd.toString());
    sr.setResponseOK();
    sr.sampleEnd();
    threadContext.getPreviousResult().addSubResult(sr);
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendIfEnabled(StringBuilder sb, StringBuilder rd, String requestData, String responseData, boolean condition, Runnable action) {
    if (condition) {
        rd.append(requestData);
        sb.append(responseData);
        action.run();
        sb.append("\n");
    }
}

