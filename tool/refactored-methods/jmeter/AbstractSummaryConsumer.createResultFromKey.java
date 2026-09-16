private MapResultData createResultFromKey(String key) {
    SummaryInfo info = key == null ? overallInfo : infos.get(key);
    TData data = info.getData();
    if (data == null) {
        return null;
    }
    MapResultData result = new MapResultData();
    result.setResult(RESULT_VALUE_IS_CONTROLLER, new ValueResultData(info.isController()));
    result.setResult(RESULT_VALUE_DATA, createDataResult(key, data));
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Indicates whether this summary can discriminate controller samples
 *
 * @return true, if this summary can discriminate controller samples; false
 *         otherwise.
 */
public final boolean supportsControllersDiscrimination() {
    return supportsControllersDiscrimination;
}

