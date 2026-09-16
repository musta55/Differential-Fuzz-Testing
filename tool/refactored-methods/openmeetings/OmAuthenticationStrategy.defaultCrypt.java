private static ICrypt defaultCrypt(String encryptionKey, String saltStr) {
    SunJceCrypt crypt = null;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos)) {
        ps.append(saltStr).append("om_secret");
        byte[] salt = Arrays.copyOfRange(baos.toByteArray(), 0, 8);
        crypt = new SunJceCrypt(salt, 1000);
        crypt.setKey(encryptionKey);
    } catch (IOException e) {
        log.error("Unexpected error while creating crypt", e);
    }
    return crypt;
}
// ---- helper method(s) introduced by the refactoring ----
private String getValueAtIndex(String[] values, int index) {
    return index < values.length ? values[index] : null;
}

