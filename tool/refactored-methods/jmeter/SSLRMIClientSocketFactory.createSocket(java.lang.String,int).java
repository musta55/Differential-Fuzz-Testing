@Override
public Socket createSocket(String host, int port) throws IOException {
    SSLContext ctx = initializeSSLContext();
    SSLSocketFactory factory = ctx.getSocketFactory();
    if (factory == null) {
        throw new IOException("Unable to obtain SSLSocketFactory for provided KeyStore");
    }
    return factory.createSocket(host, port);
}
// ---- helper method(s) introduced by the refactoring ----
private SSLContext initializeSSLContext() throws IOException {
    char[] passphrase = keyStorePassword != null ? keyStorePassword.toCharArray() : null;
    KeyStore keyStore = keyStoreLocation != null ? loadStore(keyStoreLocation, passphrase, keyStoreType) : null;
    KeyStore trustStore = trustStoreLocation != null ? loadStore(trustStoreLocation, trustStorePassword.toCharArray(), trustStoreType) : keyStore;
    if (alias == null) {
        throw new IOException("SSL certificate alias cannot be null; MUST be set for SSLServerSocketFactory!");
    }
    try {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphrase);
        SSLContext ctx = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        ctx.init(AliasKeyManager.wrap(kmf.getKeyManagers(), alias), tmf.getTrustManagers(), null);
        return ctx;
    } catch (GeneralSecurityException e) {
        throw new IOException(e);
    }
}

