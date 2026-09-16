/**
 * @see GraphValueSelector#select(String, Sample)
 */
@Override
public Double select(String series, Sample sample) {
    if (!sample.isController() && sample.getSuccess()) {
        return (double) sample.getElapsedTime();
    } else {
        return null;
    }
}