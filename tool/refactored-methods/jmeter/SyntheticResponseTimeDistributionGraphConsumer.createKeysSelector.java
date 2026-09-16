/*
     * (non-Javadoc)
     *
     * @see org.apache.jmeter.report.csv.processor.impl.AbstractGraphConsumer#
     * createKeysSelector()
     */
@Override
protected final GraphKeysSelector createKeysSelector() {
    return sample -> {
        if (sample.getSuccess()) {
            long elapsedTime = sample.getElapsedTime();
            if (elapsedTime <= satisfiedThreshold) {
                return 0d;
            } else if (elapsedTime <= toleratedThreshold) {
                return 1d;
            } else {
                return 2d;
            }
        } else {
            return 3d;
        }
    };
}
// ---- helper method(s) introduced by the refactoring ----
private static long getStaticSatisfiedThreshold() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).satisfiedThreshold;
}

private static long getStaticToleratedThreshold() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).toleratedThreshold;
}

private static List<String> getStaticSatisfiedLabels() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).satisfiedLabels;
}

private static List<String> getStaticToleratedLabels() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).toleratedLabels;
}

private static List<String> getStaticUntoleratedLabels() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).untoleratedLabels;
}

private static SyntheticResponseTimeDistributionGraphConsumer getCurrentInstance() {
    // Assuming there's a way to get the current instance, adjust accordingly
    throw new UnsupportedOperationException("Static access to instance fields is not supported.");
}

