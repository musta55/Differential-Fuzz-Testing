public ErrorMetric(SampleResult result) {
    if (MetricUtils.isSuccessCode(responseCode) || (StringUtils.isEmpty(responseCode) && !StringUtils.isEmpty(result.getFirstAssertionFailureMessage()))) {
        responseCode = MetricUtils.ASSERTION_FAILED;
        responseMessage = result.getFirstAssertionFailureMessage();
    } else {
        responseCode = result.getResponseCode();
        responseMessage = result.getResponseMessage();
    }
}