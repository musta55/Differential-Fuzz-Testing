/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.processor.ResultDataVisitor#visitListResult(
     * org.apache.jmeter.report.processor.ListResultData)
     */
@Override
public String visitListResult(ListResultData listResult) {
    String result = "";
    if (listResult != null) {
        int count = listResult.getSize();
        String[] items = new String[count];
        for (int i = 0; i < count; i++) {
            items[i] = listResult.get(i).accept(this);
        }
        result = JsonUtil.toJsonArray(items);
    }
    return result;
}