/**
 * @see org.apache.jmeter.control.GenericController#resetCurrent()
 */
@Override
protected void resetCurrent() {
    if (getSubControllers().isEmpty()) {
        current = 0;
    } else {
        current = ThreadLocalRandom.current().nextInt(getSubControllers().size());
    }
}