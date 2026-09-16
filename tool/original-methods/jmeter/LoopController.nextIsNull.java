/**
 * {@inheritDoc}
 */
@Override
protected Sampler nextIsNull() throws NextIsNullException {
    reInitialize();
    if (endOfLoop()) {
        if (!getContinueForever()) {
            setDone(true);
        } else {
            resetLoopCount();
        }
        return null;
    }
    return next();
}