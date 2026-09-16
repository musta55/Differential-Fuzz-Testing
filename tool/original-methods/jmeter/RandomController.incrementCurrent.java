/**
 * @see org.apache.jmeter.control.GenericController#incrementCurrent()
 */
@Override
protected void incrementCurrent() {
    super.incrementCurrent();
    current = ThreadLocalRandom.current().nextInt(this.getSubControllers().size());
}