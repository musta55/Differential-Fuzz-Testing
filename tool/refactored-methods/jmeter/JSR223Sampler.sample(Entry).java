@Override
public SampleResult sample(Entry entry) {
    SampleResult result = setupSampleResult();
    result.sampleStart();
    try {
        Object ret = executeScript(result);
        handleScriptResult(result, ret);
    } catch (IOException | ScriptException e) {
        log.error("Problem in JSR223 script {}, message: {}", getName(), e, e);
        result.setSuccessful(false);
        // $NON-NLS-1$
        result.setResponseCode("500");
        result.setResponseMessage(e.toString());
    }
    if (result.getEndTime() == 0) {
        result.sampleEnd();
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private SampleResult setupSampleResult() {
    SampleResult result = new SampleResult();
    result.setSampleLabel(getName());
    result.setSuccessful(true);
    result.setResponseCodeOK();
    result.setResponseMessageOK();
    final String filename = getFilename();
    if (!filename.isEmpty()) {
        result.setSamplerData("File: " + filename);
    } else {
        result.setSamplerData(getScript());
    }
    result.setDataType(SampleResult.TEXT);
    return result;
}

private Object executeScript(SampleResult result) throws IOException, ScriptException {
    ScriptEngine scriptEngine = getScriptEngine();
    Bindings bindings = scriptEngine.createBindings();
    bindings.put("SampleResult", result);
    return processFileOrScript(scriptEngine, bindings);
}

private static void handleScriptResult(SampleResult result, Object ret) {
    if (ret != null && (result.getResponseData() == null || result.getResponseData().length == 0)) {
        result.setResponseData(ret.toString(), null);
    }
}

