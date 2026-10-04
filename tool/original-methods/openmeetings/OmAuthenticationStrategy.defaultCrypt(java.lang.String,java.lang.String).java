private static ICrypt defaultCrypt(String encryptionKey, String saltStr) {
    SunJceCrypt crypt = null;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos)) {
        ps.append(saltStr).append("om_secret");
        byte[] salt = Arrays.copyOfRange(baos.toByteArray(), 0, 8);
        crypt = new SunJceCrypt(salt, 1000);
        crypt.setKey(encryptionKey);
    } catch (IOException e) {
        log.error("Enxpected error while creating crypt", e);
    }
    return crypt;
}