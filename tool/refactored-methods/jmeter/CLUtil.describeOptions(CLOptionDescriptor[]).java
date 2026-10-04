/**
 * Format options into StringBuilder and return. This is typically used to
 * print "Usage" text in response to a "--help" or invalid option.
 *
 * @param options
 *            the option descriptors
 * @return the formatted description/help for options
 */
public static StringBuilder describeOptions(final CLOptionDescriptor[] options) {
    final String lSep = System.getProperty("line.separator");
    final StringBuilder sb = new StringBuilder();
    for (CLOptionDescriptor option : options) {
        appendOption(sb, option, lSep);
    }
    return sb;
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendOption(StringBuilder sb, CLOptionDescriptor option, String lSep) {
    final char ch = (char) option.getId();
    final String name = option.getName();
    String description = option.getDescription();
    int flags = option.getFlags();
    boolean argumentOptional = (flags & CLOptionDescriptor.ARGUMENT_OPTIONAL) == CLOptionDescriptor.ARGUMENT_OPTIONAL;
    boolean argumentRequired = (flags & CLOptionDescriptor.ARGUMENT_REQUIRED) == CLOptionDescriptor.ARGUMENT_REQUIRED;
    boolean twoArgumentsRequired = (flags & CLOptionDescriptor.ARGUMENTS_REQUIRED_2) == CLOptionDescriptor.ARGUMENTS_REQUIRED_2;
    sb.append('\t');
    appendOptionName(sb, ch, name);
    appendArgumentDetails(sb, argumentOptional, argumentRequired, twoArgumentsRequired);
    sb.append(lSep);
    appendDescription(sb, description, lSep);
}

private static void appendOptionName(StringBuilder sb, char ch, String name) {
    boolean needComma = false;
    if (Character.isLetter(ch)) {
        sb.append("-").append(ch);
        needComma = true;
    }
    if (name != null) {
        if (needComma) {
            sb.append(", ");
        }
        sb.append("--").append(name);
    }
}

private static void appendArgumentDetails(StringBuilder sb, boolean argumentOptional, boolean argumentRequired, boolean twoArgumentsRequired) {
    if (argumentOptional) {
        sb.append(" [<argument>]");
    }
    if (argumentRequired) {
        sb.append(" <argument>");
    }
    if (twoArgumentsRequired) {
        sb.append("=<value>");
    }
}

private static void appendDescription(StringBuilder sb, String description, String lSep) {
    if (description != null) {
        while (description.length() > MAX_DESCRIPTION_COLUMN_LENGTH) {
            String descriptionPart = description.substring(0, MAX_DESCRIPTION_COLUMN_LENGTH);
            description = description.substring(MAX_DESCRIPTION_COLUMN_LENGTH);
            sb.append("\t\t").append(descriptionPart).append(lSep);
        }
        sb.append("\t\t").append(description).append(lSep);
    }
}

