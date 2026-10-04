@Override
public ServerSocket createServerSocket(int port) throws IOException {
    SSLContext sslContext = createSSLContext();
    SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
    if (factory == null) {
        throw new IOException("Unable to obtain SSLServerSocketFactory for provided KeyStore");
    }
    SSLServerSocket socket;
    try {
        socket = (SSLServerSocket) factory.createServerSocket(port, 0, localAddress);
    } catch (BindException e) {
        throw new IOException("Could not bind to " + localAddress + " using port " + port, e);
    }
    socket.setNeedClientAuth(clientAuth);
    LOGGER.info("Created SSLSocket: {}", socket);
    return socket;
}
// ---- helper method(s) introduced by the refactoring ----
private SSLContext createSSLContext() throws IOException {
    char[] passphrase = keyStorePassword != null ? keyStorePassword.toCharArray() : null;
    KeyStore keyStore = keyStoreLocation != null ? loadStore(keyStoreLocation, passphrase, keyStoreType) : null;
    KeyStore trustStore = trustStoreLocation != null ? loadStore(trustStoreLocation, trustStorePassword.toCharArray(), trustStoreType) : keyStore;
    if (alias == null) {
        throw new IOException("SSL certificate alias cannot be null; MUST be set for SSLServerSocketFactory!");
    }
    try {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(AliasKeyManager.wrap(kmf.getKeyManagers(), alias), tmf.getTrustManagers(), null);
        return ctx;
    } catch (GeneralSecurityException e) {
        throw new IOException(e);
    }
}

