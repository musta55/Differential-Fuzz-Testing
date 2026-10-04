@Override
public void process() {
    StringBuilder sb = new StringBuilder(100);
    // for request Data
    StringBuilder rd = new StringBuilder(20);
    SampleResult sr = new SampleResult();
    sr.setSampleLabel(getName());
    sr.sampleStart();
    JMeterContext threadContext = getThreadContext();
    if (isDisplaySamplerProperties()) {
        rd.append("SamplerProperties\n");
        sb.append("SamplerProperties:\n");
        formatPropertyIterator(sb, threadContext.getCurrentSampler().propertyIterator());
        sb.append("\n");
    }
    if (isDisplayJMeterVariables()) {
        rd.append("JMeterVariables\n");
        sb.append("JMeterVariables:\n");
        formatSet(sb, threadContext.getVariables().entrySet());
        sb.append("\n");
    }
    if (isDisplayJMeterProperties()) {
        rd.append("JMeterProperties\n");
        sb.append("JMeterProperties:\n");
        formatSet(sb, JMeterUtils.getJMeterProperties().entrySet());
        sb.append("\n");
    }
    if (isDisplaySystemProperties()) {
        rd.append("SystemProperties\n");
        sb.append("SystemProperties:\n");
        formatSet(sb, System.getProperties().entrySet());
        sb.append("\n");
    }
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