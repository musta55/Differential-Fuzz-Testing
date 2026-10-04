public static void getLine(StringBuilder sb, String text, char fill) {
    sb.append("\t#");
    appendFillCharacters(sb, calculateHeadLength(text), fill);
    sb.append(text);
    appendFillCharacters(sb, calculateTailLength(text), fill);
    sb.append("#\n");
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendFillCharacters(StringBuilder sb, int length, char fill) {
    for (int i = 0; i < length; ++i) {
        sb.append(fill);
    }
}

private static int calculateHeadLength(String text) {
    return (TOTAL_LENGTH - text.length()) / 2;
}

private static int calculateTailLength(String text) {
    return TOTAL_LENGTH - text.length() - calculateHeadLength(text);
}

