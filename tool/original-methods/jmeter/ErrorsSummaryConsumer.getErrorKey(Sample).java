/**
 * @param sample {@link Sample}
 * @return String Error key for sample
 */
static String getErrorKey(Sample sample) {
    if (sample.getSuccess()) {
        return "";
    }
    String responseCode = sample.getResponseCode();
    String responseMessage = sample.getResponseMessage();
    String key = responseCode + (!StringUtils.isEmpty(responseMessage) ? "/" + escapeJson(responseMessage) : "");
    if (MetricUtils.isSuccessCode(responseCode) || (StringUtils.isEmpty(responseCode) && StringUtils.isNotBlank(sample.getFailureMessage()))) {
        key = MetricUtils.ASSERTION_FAILED;
        if (ASSERTION_RESULTS_FAILURE_MESSAGE) {
            String msg = sample.getFailureMessage();
            if (StringUtils.isNotBlank(msg)) {
                key = escapeJson(msg);
            }
        }
    }
    return key;
}