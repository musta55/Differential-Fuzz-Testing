/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.core.AbstractAggregatorFactory#createAggregator
     * ()
     */
@Override
protected Aggregator createAggregator() {
    Aggregator newAggregator = null;
    if (lastAggregator != null) {
        newAggregator = new PercentileAggregator((PercentileAggregator) lastAggregator);
    } else {
        newAggregator = new PercentileAggregator(percentileIndex);
    }
    lastAggregator = newAggregator;
    return newAggregator;
}