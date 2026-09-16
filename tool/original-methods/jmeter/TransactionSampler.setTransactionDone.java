protected void setTransactionDone() {
    this.transactionDone = true;
    // Set the overall status for the transaction sample
    // TODO: improve, e.g. by adding counts to the SampleResult class
    transactionSampleResult.setResponseMessage(TransactionController.NUMBER_OF_SAMPLES_IN_TRANSACTION_PREFIX + calls + ", number of failing samples : " + noFailingSamples);
    if (transactionSampleResult.isSuccessful()) {
        transactionSampleResult.setResponseCodeOK();
    }
    // Bug 50080 (not include pause time when generate parent)
    if (!transactionController.isIncludeTimers()) {
        long end = transactionSampleResult.currentTimeInMillis();
        transactionSampleResult.setIdleTime(end - transactionSampleResult.getStartTime() - totalTime);
        transactionSampleResult.setEndTime(end);
    }
    transactionSampleResult.setConnectTime(totalConnectTime);
}