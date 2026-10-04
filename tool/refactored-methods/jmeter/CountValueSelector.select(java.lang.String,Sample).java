/**
 * @see GraphValueSelector#select(String, Sample)
 */
@Override
public Double select(String series, Sample sample) {
    boolean shouldCount = isIgnoreTransactionController() ? !sample.isController() : !sample.isEmptyController();
    return shouldCount ? ONE : ZERO;
}