/**
 * @see GraphValueSelector#select(String, Sample)
 */
@Override
public Double select(String series, Sample sample) {
    if (isIgnoreTransactionController()) {
        if (!sample.isController()) {
            return ONE;
        }
    } else {
        if (!sample.isEmptyController()) {
            return ONE;
        }
    }
    return ZERO;
}