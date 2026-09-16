/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.ResultDataVisitor#visitMapResult(org
     * .apache.jmeter.report.processor.MapResultData)
     */
@Override
public String visitMapResult(MapResultData mapResult) {
    if (mapResult == null) {
        return "";
    }
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<String, ResultData> entry : mapResult.entrySet()) {
        map.put(entry.getKey(), entry.getValue().accept(this));
    }
    return JsonUtil.toJsonObject(map);
}
// ---- helper method(s) introduced by the refactoring ----
private static String handleStringValue(String result) {
    return '"' + new String(JsonStringEncoder.getInstance().quoteAsString(result)) + '"';
}

