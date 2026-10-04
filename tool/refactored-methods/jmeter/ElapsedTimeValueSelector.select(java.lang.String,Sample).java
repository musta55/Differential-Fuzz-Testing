/*
     * (non-Javadoc)
     *
     * @see GraphValueSelector#select(String, Sample)
     */
@Override
public Double select(String series, Sample sample) {
    if (!isIgnoreTransactionController() && !sample.isEmptyController() || isIgnoreTransactionController() && !sample.isController()) {
        return (double) sample.getElapsedTime();
    }
    return null;
}