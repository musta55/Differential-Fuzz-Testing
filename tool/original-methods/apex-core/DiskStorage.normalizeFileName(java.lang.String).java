public static String normalizeFileName(String name) {
    StringBuilder sb = new StringBuilder(1024);
    for (int i = 0; i < name.length(); i++) {
        Character c = name.charAt(i);
        if (Character.isLetterOrDigit(c)) {
            sb.append(c);
        } else {
            sb.append('-');
        }
    }
    return sb.toString();
}