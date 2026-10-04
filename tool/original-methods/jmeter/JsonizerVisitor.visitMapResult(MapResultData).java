/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.ResultDataVisitor#visitMapResult(org
     * .apache.jmeter.report.processor.MapResultData)
     */
@Override
public String visitMapResult(MapResultData mapResult) {
    String result = "";
    if (mapResult != null) {
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String, ResultData> entry : mapResult.entrySet()) {
            map.put(entry.getKey(), entry.getValue().accept(this));
        }
        result = JsonUtil.toJsonObject(map);
    }
    return result;
}