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
        MongoScriptRunner runner = new MongoScriptRunner();
        DB db = mongoDB.getDB(getDatabase(), getUsername(), getPassword());
        res.latencyEnd();
        Object result = runner.evaluate(db, data);
        EvalResultHandler handler = new EvalResultHandler();
        String resultAsString = handler.handle(result);
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