/**
 * {@inheritDoc}
 */
@Override
protected void currentReturnedNull(Controller c) {
    handleCurrentReturnedNull(c);
}
// ---- helper method(s) introduced by the refactoring ----
private void handleCurrentReturnedNull(Controller c) {
    if (c.isDone()) {
        removeCurrentElement();
    } else if (getStyle() == USE_SUB_CONTROLLERS) {
        incrementCurrent();
    }
}

private void handleControllerStyle() {
    if (getStyle() == IGNORE_SUB_CONTROLLERS) {
        incrementCurrent();
        skipNext = true;
    } else {
        searchStart = null;
    }
}

