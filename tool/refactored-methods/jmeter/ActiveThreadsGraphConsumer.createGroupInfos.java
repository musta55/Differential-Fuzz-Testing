/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.csv.processor.impl.AbstractGraphConsumer#
     * createGroupInfos()
     */
@Override
protected Map<String, GroupInfo> createGroupInfos() {
    AbstractSeriesSelector seriesSelector = new ThreadNameSeriesSelector();
    GraphValueSelector graphValueSelector = new GroupThreadsGraphValueSelector();
    return Collections.singletonMap(AbstractGraphConsumer.DEFAULT_GROUP, new GroupInfo(new MeanAggregatorFactory(), seriesSelector, graphValueSelector, false, false));
}