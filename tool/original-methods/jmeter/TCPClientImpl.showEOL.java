private static String showEOL(final String input) {
    StringBuilder sb = new StringBuilder(input.length() * 2);
    for (int i = 0; i < input.length(); i++) {
        char ch = input.charAt(i);
        if (ch < ' ') {
            sb.append('[');
            sb.append((int) ch);
            sb.append(']');
        } else {
            sb.append(ch);
        }
    }
    return sb.toString();
}