/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.csv.processor.impl.AbstractGraphConsumer#
     * createGroupInfos()
     */
@Override
protected Map<String, GroupInfo> createGroupInfos() {
    AbstractSeriesSelector seriesSelector = new AbstractSeriesSelector() {

        @Override
        public Iterable<String> select(Sample sample) {
            if (sample.isEmptyController()) {
                return Collections.emptyList();
            }
            String threadName = sample.getThreadName();
            int index = threadName.lastIndexOf(' ');
            if (index >= 0) {
                threadName = threadName.substring(0, index);
            }
            return Collections.singletonList(threadName);
        }
    };
    GraphValueSelector graphValueSelector = (series, sample) -> {
        if (!sample.isEmptyController()) {
            return (double) sample.getGroupThreads();
        } else {
            return null;
        }
    };
    return Collections.singletonMap(AbstractGraphConsumer.DEFAULT_GROUP, new GroupInfo(new MeanAggregatorFactory(), seriesSelector, graphValueSelector, false, false));
}