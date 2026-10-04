/**
 * {@inheritDoc}
 */
@Override
protected Sampler nextIsAController(Controller controller) throws NextIsNullException {
    Sampler sampler = controller.next();
    if (sampler == null) {
        handleCurrentReturnedNull(controller);
        return next();
    }
    currentReturnedAtLeastOne = true;
    handleControllerStyle();
    return sampler;
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

