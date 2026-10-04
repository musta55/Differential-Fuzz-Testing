public LocalTrustStoreSSLSocketFactory(File truststore) {
    SSLContext sslcontext = null;
    try {
        // $NON-NLS-1$
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fileStream = new FileInputStream(truststore);
            InputStream stream = new BufferedInputStream(fileStream)) {
            ks.load(stream, null);
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        TrustManager[] trustmanagers = tmf.getTrustManagers();
        // $NON-NLS-1$
        sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, trustmanagers, new SecureRandom());
    } catch (Exception e) {
        handleException(e);
    }
    factory = sslcontext.getSocketFactory();
}
// ---- helper method(s) introduced by the refactoring ----
private static void handleException(Exception e) {
    throw new RuntimeException("Could not create the SSL context", e);
}

