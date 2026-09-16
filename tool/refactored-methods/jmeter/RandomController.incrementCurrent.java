/**
 * @see org.apache.jmeter.control.GenericController#incrementCurrent()
 */
@Override
protected void incrementCurrent() {
    current = ThreadLocalRandom.current().nextInt(getSubControllers().size());
}