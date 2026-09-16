/**
 * Standard constructor
 */
public TrustAllSSLSocketFactory() {
    SSLContext sslcontext = initializeSSLContext();
    factory = sslcontext.getSocketFactory();
}
// ---- helper method(s) introduced by the refactoring ----
private static SSLContext initializeSSLContext() {
    try {
        // $NON-NLS-1$
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[] { new X509ExtendedTrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return EMPTY_X509_CERTIFICATE;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
                // NOSONAR JMeter is a pentest and perf testing tool
                // NOOP
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
                // NOSONAR JMeter is a pentest and perf testing tool
                // NOOP
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
                // NOOP
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
                // NOOP
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {
                // NOOP
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {
                // NOOP
            }
        } }, new java.security.SecureRandom());
        return sslcontext;
    } catch (Exception e) {
        throw new IllegalStateException("Could not create the SSL context", e);
    }
}

