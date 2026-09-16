public ErrorMetric(SampleResult result) {
    responseCode = result.getResponseCode();
    responseMessage = result.getResponseMessage();
    if (MetricUtils.isSuccessCode(responseCode) || StringUtils.isEmpty(responseCode)) {
        responseCode = MetricUtils.ASSERTION_FAILED;
        responseMessage = StringUtils.defaultIfEmpty(result.getFirstAssertionFailureMessage(), "");
    }
}