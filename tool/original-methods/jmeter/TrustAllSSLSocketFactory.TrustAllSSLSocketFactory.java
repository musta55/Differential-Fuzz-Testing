/**
 * Standard constructor
 */
public TrustAllSSLSocketFactory() {
    SSLContext sslcontext = null;
    try {
        // $NON-NLS-1$
        sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[] { new X509ExtendedTrustManager() {

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return EMPTY_X509Certificate;
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
    } catch (Exception e) {
        throw new IllegalStateException("Could not create the SSL context", e);
    }
    factory = sslcontext.getSocketFactory();
}