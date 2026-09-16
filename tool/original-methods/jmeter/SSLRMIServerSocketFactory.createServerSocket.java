@Override
public ServerSocket createServerSocket(int port) throws IOException {
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
    SSLContext ctx;
    try {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, passphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        ctx = SSLContext.getInstance("TLS");
        ctx.init(AliasKeyManager.wrap(kmf.getKeyManagers(), alias), tmf.getTrustManagers(), null);
    } catch (GeneralSecurityException e) {
        throw new IOException(e);
    }
    SSLServerSocketFactory factory = ctx.getServerSocketFactory();
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