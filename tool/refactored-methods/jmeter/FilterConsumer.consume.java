@Override
public void consume(Sample sample, int channel) {
    boolean matches = samplePredicate != null && samplePredicate.matches(sample);
    if (reverseFilter ? !matches : matches) {
        super.produce(sample, channel);
    }
}