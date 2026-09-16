/**
 * {@inheritDoc}
 */
@Override
public String toString() {
    StringBuilder result = new StringBuilder("conversation-key\n");
    result.append("\n");
    result.append("\tgroup:\t\t");
    result.append(this.groupKey.getName());
    result.append("\n");
    result.append("\tqualifiers:\t");
    if (qualifiers != null) {
        for (Annotation qualifier : this.qualifiers) {
            result.append(qualifier.annotationType().getName());
            result.append(" ");
        }
    } else {
        result.append("---");
    }
    return result.toString();
}