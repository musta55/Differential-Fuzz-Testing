/**
 * {@inheritDoc}
 */
@Override
public SampleResult sample(Entry e) {
    trace("sample()");
    SampleResult res = new SampleResult();
    // Did sample succeed?
    boolean isOK = false;
    // Sampler data
    String data = getData();
    String response = null;
    res.setSampleLabel(getTitle());
    /*
         * Perform the sampling
         */
    // Start timing
    res.sampleStart();
    try {
        // Do something here ...
        response = Thread.currentThread().getName();
        /*
             * Set up the sample result details
             */
        res.setSamplerData(data);
        res.setResponseData(response, null);
        res.setDataType(SampleResult.TEXT);
        res.setResponseCodeOK();
        // $NON-NLS-1$
        res.setResponseMessage("OK");
        isOK = true;
    } catch (Exception ex) {
        log.debug("", ex);
        // $NON-NLS-1$
        res.setResponseCode("500");
        res.setResponseMessage(ex.toString());
    }
    // End timing
    res.sampleEnd();
    res.setSuccessful(isOK);
    return res;
}