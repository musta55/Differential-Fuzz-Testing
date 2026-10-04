public static void getLine(StringBuilder sb, String text, char fill) {
    sb.append("\t#");
    int l = text.length();
    int headLength = (TOTAL_LENGTH - l) / 2;
    for (int i = 0; i < headLength; ++i) {
        sb.append(fill);
    }
    sb.append(text);
    for (int i = 0; i < (TOTAL_LENGTH - l - headLength); ++i) {
        sb.append(fill);
    }
    sb.append("#\n");
}