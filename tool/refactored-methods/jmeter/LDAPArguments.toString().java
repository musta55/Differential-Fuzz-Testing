/**
 * Create a string representation of the arguments.
 *
 * @return the string representation of the arguments
 */
@Override
public String toString() {
    StringBuilder str = new StringBuilder();
    PropertyIterator iter = getArguments().iterator();
    while (iter.hasNext()) {
        LDAPArgument arg = (LDAPArgument) iter.next().getObjectValue();
        appendArgumentToString(str, arg);
        if (iter.hasNext()) {
            //$NON-NLS$
            str.append("&");
        }
    }
    return str.toString();
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Append an LDAPArgument to the StringBuilder.
 *
 * @param str the StringBuilder to append to
 * @param arg the LDAPArgument to append
 */
private static void appendArgumentToString(StringBuilder str, LDAPArgument arg) {
    final String metaData = arg.getMetaData();
    str.append(arg.getName());
    if (metaData == null) {
        //$NON-NLS$
        str.append("=");
    } else {
        str.append(metaData);
    }
    str.append(arg.getValue());
}

