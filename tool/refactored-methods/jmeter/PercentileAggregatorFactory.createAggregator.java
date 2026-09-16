/*
     * (non-Javadoc)
     *
     * @see
     * org.apache.jmeter.report.core.AbstractAggregatorFactory#createAggregator
     * ()
     */
@Override
protected Aggregator createAggregator() {
    Aggregator newAggregator = createNewAggregator();
    lastAggregator = newAggregator;
    return newAggregator;
}
// ---- helper method(s) introduced by the refactoring ----
private Aggregator createNewAggregator() {
    if (lastAggregator != null) {
        return new PercentileAggregator((PercentileAggregator) lastAggregator);
    } else {
        return new PercentileAggregator(percentileIndex);
    }
}

