/**
 * {@inheritDoc}
 */
@Override
protected void currentReturnedNull(Controller c) {
    if (c.isDone()) {
        removeCurrentElement();
    } else if (getStyle() == USE_SUB_CONTROLLERS) {
        incrementCurrent();
    }
}