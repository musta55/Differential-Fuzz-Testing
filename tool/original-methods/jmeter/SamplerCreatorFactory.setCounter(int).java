/**
 * Set the counter for all available {@link SamplerCreator}s.
 * <p>
 * <em>The only implementation that is currently available, increments the counter before it is used!</em>
 * @param value to initialize the creators
 */
public void setCounter(int value) {
    DEFAULT_SAMPLER_CREATOR.setCounter(value);
    for (SamplerCreator samplerCreator : samplerCreatorMap.values()) {
        samplerCreator.setCounter(value);
    }
}