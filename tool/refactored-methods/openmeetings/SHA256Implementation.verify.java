static boolean verify(String str, String hash) {
    if (str == null) {
        return hash == null;
    }
    if (hash == null) {
        return false;
    }
    String[] parts = parseHash(hash);
    if (parts == null) {
        return false;
    }
    return verifyHash(str, parts);
}
// ---- helper method(s) introduced by the refactoring ----
private static String[] parseHash(String hash) {
    String[] parts = hash.split(":");
    return parts.length == 3 ? parts : null;
}

private static boolean verifyHash(String str, String[] parts) {
    try {
        int iter = Integer.parseInt(parts[0]);
        String expectedHash = parts[1];
        byte[] salt = Base64.decodeBase64(parts[2]);
        String actualHash = hash(str, salt, iter);
        return actualHash.equals(expectedHash);
    } catch (Exception e) {
        return false;
    }
}

