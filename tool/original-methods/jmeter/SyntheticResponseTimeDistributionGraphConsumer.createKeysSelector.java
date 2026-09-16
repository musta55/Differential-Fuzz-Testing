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
                return (double) 0;
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