/*
     * Convert to a shorter String for test purposes
     *
     * @return the string value
     */
final String toShortString() {
    final StringBuilder sb = new StringBuilder();
    final char id = (char) this.descriptor.getId();
    if (id != TEXT_ARGUMENT) {
        sb.append("-");
        sb.append(id);
    }
    if (this.arguments != null) {
        if (id != TEXT_ARGUMENT) {
            sb.append("=");
        }
        sb.append(this.arguments);
    }
    return sb.toString();
}