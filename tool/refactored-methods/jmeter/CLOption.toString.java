/**
 * Convert to String.
 *
 * @return the string value
 */
@Override
public final String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("[");
    final char id = (char) this.descriptor.getId();
    if (id == TEXT_ARGUMENT) {
        sb.append("TEXT ");
    } else {
        sb.append("Option ");
        sb.append(id);
    }
    if (this.arguments != null) {
        sb.append(", ");
        sb.append(this.arguments);
    }
    sb.append(" ]");
    return sb.toString();
}