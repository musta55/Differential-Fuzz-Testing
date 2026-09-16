public void addSubSamplerResult(SampleResult res) {
    // Another subsample for the transaction
    calls++;
    // Set Response code of transaction
    if (noFailingSamples == 0) {
        transactionSampleResult.setResponseCode(res.getResponseCode());
    }
    // The transaction fails if any sub sample fails
    if (!res.isSuccessful()) {
        transactionSampleResult.setSuccessful(false);
        noFailingSamples++;
    }
    // Add the sub result to the transaction result
    transactionSampleResult.addSubResult(res, false);
    // Add current time to total for later use (exclude pause time)
    totalTime += res.getTime();
    totalConnectTime += res.getConnectTime();
}