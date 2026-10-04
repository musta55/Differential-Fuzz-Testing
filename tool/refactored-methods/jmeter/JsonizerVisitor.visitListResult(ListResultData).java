/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.ResultDataVisitor#visitListResult(
     * org.apache.jmeter.report.processor.ListResultData)
     */
@Override
public String visitListResult(ListResultData listResult) {
    if (listResult == null) {
        return "";
    }
    String[] items = new String[listResult.getSize()];
    for (int i = 0; i < items.length; i++) {
        items[i] = listResult.get(i).accept(this);
    }
    return JsonUtil.toJsonArray(items);
}
// ---- helper method(s) introduced by the refactoring ----
private static String handleStringValue(String result) {
    return '"' + new String(JsonStringEncoder.getInstance().quoteAsString(result)) + '"';
}

