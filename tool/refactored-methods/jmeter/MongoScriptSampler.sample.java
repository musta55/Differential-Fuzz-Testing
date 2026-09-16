@Override
public SampleResult sample(Entry e) {
    trace("sample()");
    SampleResult res = new SampleResult();
    String data = getScript();
    res.setSampleLabel(getTitle());
    res.setResponseCodeOK();
    // $NON-NLS-1$
    res.setResponseCode("200");
    res.setSuccessful(true);
    res.setResponseMessageOK();
    res.setSamplerData(data);
    res.setDataType(SampleResult.TEXT);
    // $NON-NLS-1$
    res.setContentType("text/plain");
    res.sampleStart();
    try {
        MongoDB mongoDB = MongoSourceElement.getMongoDB(getSource());
        DB db = connectToDatabase(mongoDB);
        res.latencyEnd();
        Object result = evaluateScript(db, data);
        String resultAsString = handleEvalResult(result);
        res.setResponseData(resultAsString, res.getDataEncodingWithDefault());
    } catch (Exception ex) {
        // $NON-NLS-1$
        res.setResponseCode("500");
        res.setSuccessful(false);
        res.setResponseMessage(ex.toString());
        res.setResponseData(ex.getMessage(), res.getDataEncodingWithDefault());
    } finally {
        res.sampleEnd();
    }
    return res;
}
// ---- helper method(s) introduced by the refactoring ----
private DB connectToDatabase(MongoDB mongoDB) {
    return mongoDB.getDB(getDatabase(), getUsername(), getPassword());
}

private static Object evaluateScript(DB db, String script) throws Exception {
    MongoScriptRunner runner = new MongoScriptRunner();
    return runner.evaluate(db, script);
}

private static String handleEvalResult(Object result) {
    EvalResultHandler handler = new EvalResultHandler();
    return handler.handle(result);
}

