/**
 * {@inheritDoc}
 */
@Override
public Sampler next() {
    updateIterationIndex(getName(), loopCount);
    try {
        if (isEndOfLoop()) {
            handleEndOfLoop();
            return null;
        }
        return super.next();
    } finally {
        updateIterationIndex(getName(), loopCount);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldEvaluateLoops() {
    return nbLoops == null || nbLoops == 0 || nbLoops == INFINITE_LOOP_COUNT;
}

private boolean isEndOfLoop() {
    return breakLoop || (getLoops() > INFINITE_LOOP_COUNT && loopCount >= getLoops());
}

private void handleEndOfLoop() {
    if (!getContinueForever()) {
        setDone(true);
    }
    resetBreakLoop();
}

private void handleEndOfLoopForNextIsNull() {
    if (!getContinueForever()) {
        setDone(true);
    } else {
        resetLoopCount();
    }
}

