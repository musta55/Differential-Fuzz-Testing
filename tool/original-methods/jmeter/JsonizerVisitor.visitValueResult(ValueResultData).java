/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.ResultDataVisitor#visitValueResult
     * (org.apache.jmeter.report.processor.ValueResultData)
     */
@Override
public String visitValueResult(ValueResultData valueResult) {
    String result = "";
    if (valueResult != null) {
        Object value = valueResult.getValue();
        result = String.valueOf(value);
        if (value instanceof String) {
            result = '"' + new String(JsonStringEncoder.getInstance().quoteAsString(result)) + '"';
        }
    }
    return result;
}