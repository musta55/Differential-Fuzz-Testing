/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.processor.SampleConsumer#stopConsuming()
     */
@Override
public void stopConsuming() {
    MapResultData result = new MapResultData();
    result.setResult("KoPercent", new ValueResultData((double) errorCount * 100 / count));
    result.setResult("OkPercent", new ValueResultData((double) (count - errorCount) * 100 / count));
    setDataToContext(getName(), result);
    super.stopProducing();
}