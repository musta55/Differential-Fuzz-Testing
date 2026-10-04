/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.ResultDataVisitor#visitValueResult
     * (org.apache.jmeter.report.processor.ValueResultData)
     */
@Override
public String visitValueResult(ValueResultData valueResult) {
    if (valueResult == null) {
        return "";
    }
    Object value = valueResult.getValue();
    String result = String.valueOf(value);
    if (value instanceof String) {
        result = handleStringValue(result);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static String handleStringValue(String result) {
    return '"' + new String(JsonStringEncoder.getInstance().quoteAsString(result)) + '"';
}

