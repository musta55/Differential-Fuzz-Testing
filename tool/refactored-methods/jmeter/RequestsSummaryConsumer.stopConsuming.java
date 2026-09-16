/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.processor.SampleConsumer#stopConsuming()
     */
@Override
public void stopConsuming() {
    MapResultData result = new MapResultData();
    result.setResult("KoPercent", new ValueResultData(calculatePercentage(errorCount, count)));
    result.setResult("OkPercent", new ValueResultData(calculatePercentage(count - errorCount, count)));
    setDataToContext(getName(), result);
    super.stopProducing();
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Calculates the percentage of a part relative to a whole.
 *
 * @param part the part value
 * @param whole the whole value
 * @return the percentage of the part relative to the whole
 */
private static double calculatePercentage(long part, long whole) {
    return whole == 0 ? 0.0 : (double) part * 100 / whole;
}

