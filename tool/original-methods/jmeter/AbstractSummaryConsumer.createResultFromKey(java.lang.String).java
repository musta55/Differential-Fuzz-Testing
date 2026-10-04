private MapResultData createResultFromKey(String key) {
    SummaryInfo info = (key == null) ? overallInfo : infos.get(key);
    MapResultData result = null;
    TData data = info.getData();
    if (data != null) {
        result = new MapResultData();
        result.setResult(RESULT_VALUE_IS_CONTROLLER, new ValueResultData(info.isController()));
        result.setResult(RESULT_VALUE_DATA, createDataResult(key, data));
    }
    return result;
}