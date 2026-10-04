/**
 * @see GraphValueSelector#select(String, Sample)
 */
@Override
public Double select(String series, Sample sample) {
    if (isValidSample(sample)) {
        return (double) sample.getElapsedTime();
    } else {
        return null;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isValidSample(Sample sample) {
    return !sample.isController() && sample.getSuccess();
}

