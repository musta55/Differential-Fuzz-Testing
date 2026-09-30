public static String normalizeFileName(String name) {
    StringBuilder sb = new StringBuilder(1024);
    for (char c : name.toCharArray()) {
        if (Character.isLetterOrDigit(c)) {
            sb.append(c);
        } else {
            sb.append('-');
        }
    }
    return sb.toString();
}