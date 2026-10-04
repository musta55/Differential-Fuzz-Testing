/**
 * {@inheritDoc}
 */
@Override
public Sampler next() {
    updateIterationIndex(getName(), loopCount);
    try {
        if (endOfLoop()) {
            if (!getContinueForever()) {
                setDone(true);
            }
            resetBreakLoop();
            return null;
        }
        return super.next();
    } finally {
        updateIterationIndex(getName(), loopCount);
    }
}