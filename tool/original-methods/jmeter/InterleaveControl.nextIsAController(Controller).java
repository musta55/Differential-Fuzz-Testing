/**
 * {@inheritDoc}
 */
@Override
protected Sampler nextIsAController(Controller controller) throws NextIsNullException {
    Sampler sampler = controller.next();
    if (sampler == null) {
        currentReturnedNull(controller);
        return next();
    }
    currentReturnedAtLeastOne = true;
    if (getStyle() == IGNORE_SUB_CONTROLLERS) {
        incrementCurrent();
        skipNext = true;
    } else {
        searchStart = null;
    }
    return sampler;
}