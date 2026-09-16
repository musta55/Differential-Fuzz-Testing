public void addSubSamplerResult(SampleResult res) {
    incrementCalls();
    updateResponseCode(res);
    handleFailure(res);
    addSubResult(res);
    updateTotalTimes(res);
}
// ---- helper method(s) introduced by the refactoring ----
private void incrementCalls() {
    calls++;
}

private void updateResponseCode(SampleResult res) {
    if (noFailingSamples == 0) {
        transactionSampleResult.setResponseCode(res.getResponseCode());
    }
}

private void handleFailure(SampleResult res) {
    if (!res.isSuccessful()) {
        transactionSampleResult.setSuccessful(false);
        noFailingSamples++;
    }
}

private void addSubResult(SampleResult res) {
    transactionSampleResult.addSubResult(res, false);
}

private void updateTotalTimes(SampleResult res) {
    totalTime += res.getTime();
    totalConnectTime += res.getConnectTime();
}

private void setTransactionStatus() {
    if (transactionSampleResult.isSuccessful()) {
        transactionSampleResult.setResponseCodeOK();
    }
}

private void setResponseMessage() {
    transactionSampleResult.setResponseMessage(TransactionController.NUMBER_OF_SAMPLES_IN_TRANSACTION_PREFIX + calls + ", number of failing samples : " + noFailingSamples);
}

private void setResponseCode() {
    if (transactionSampleResult.isSuccessful()) {
        transactionSampleResult.setResponseCodeOK();
    }
}

private void setIdleTime() {
    if (!transactionController.isIncludeTimers()) {
        long end = transactionSampleResult.currentTimeInMillis();
        transactionSampleResult.setIdleTime(end - transactionSampleResult.getStartTime() - totalTime);
        transactionSampleResult.setEndTime(end);
    }
}

private void setConnectTime() {
    transactionSampleResult.setConnectTime(totalConnectTime);
}

