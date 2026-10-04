@Override
public void consume(Sample sample, int channel) {
    // The sample is reproduced if :
    // A predicate is defined and the sample matches it when reverseFilter
    // is false.
    // OR
    // None predicate is defined or the sample does not match when
    // reverseFilter is true.
    if ((!reverseFilter && samplePredicate != null && samplePredicate.matches(sample)) || (reverseFilter && (samplePredicate == null || !samplePredicate.matches(sample)))) {
        super.produce(sample, channel);
    }
}