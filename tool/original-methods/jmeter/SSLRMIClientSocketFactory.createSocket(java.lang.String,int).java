@Override
public Socket createSocket(String host, int port) throws IOException {
    char[] passphrase = null;
    if (keyStorePassword != null) {
        passphrase = keyStorePassword.toCharArray();
    }
    KeyStore keyStore = null;
    if (keyStoreLocation != null) {
        keyStore = loadStore(keyStoreLocation, passphrase, keyStoreType);
    }
    KeyStore trustStore;
    if (trustStoreLocation != null) {
        trustStore = loadStore(trustStoreLocation, trustStorePassword.toCharArray(), trustStoreType);
    } else {
        trustStore = keyStore;
    }
    if (alias == null) {
        throw new IOException("SSL certificate alias cannot be null; MUST be set for SSLServerSocketFactory!");
    }
    KeyManagerFactory kmf;
    SSLContext ctx;
    try {
        kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphrase);
        ctx = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        ctx.init(AliasKeyManager.wrap(kmf.getKeyManagers(), alias), tmf.getTrustManagers(), null);
    } catch (GeneralSecurityException e) {
        throw new IOException(e);
    }
    SSLSocketFactory factory = ctx.getSocketFactory();
    if (factory == null) {
        throw new IOException("Unable to obtain SSLSocketFactory for provided KeyStore");
    }
    return factory.createSocket(host, port);
}