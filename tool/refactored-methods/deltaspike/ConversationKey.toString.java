/**
 * {@inheritDoc}
 */
@Override
public String toString() {
    StringBuilder result = new StringBuilder("conversation-key\n");
    result.append("\n").append("\tgroup:\t\t").append(this.groupKey.getName()).append("\n").append("\tqualifiers:\t");
    if (qualifiers.isEmpty()) {
        result.append("---");
    } else {
        for (Annotation qualifier : this.qualifiers) {
            result.append(qualifier.annotationType().getName()).append(" ");
        }
    }
    return result.toString();
}