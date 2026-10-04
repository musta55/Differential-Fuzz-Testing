/*
     * (non-Javadoc)
     *
     * @see GraphValueSelector#select(String, Sample)
     */
@Override
public Double select(String series, Sample sample) {
    if (isIgnoreTransactionController()) {
        if (!sample.isController()) {
            return (double) sample.getElapsedTime();
        }
    } else {
        if (!sample.isEmptyController()) {
            return (double) sample.getElapsedTime();
        }
    }
    return null;
}