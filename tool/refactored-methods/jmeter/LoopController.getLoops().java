public int getLoops() {
    if (shouldEvaluateLoops()) {
        try {
            nbLoops = get(getSchema().getLoops());
        } catch (NumberFormatException e) {
            nbLoops = 0;
        }
    }
    return nbLoops;
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

