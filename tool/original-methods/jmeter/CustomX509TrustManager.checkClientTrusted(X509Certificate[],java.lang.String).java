/**
 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String)
 */
@Override
public void checkClientTrusted(X509Certificate[] certificates, String authType) {
    // NOSONAR JMeter is a pentest and perf testing tool
    if (log.isDebugEnabled() && certificates != null) {
        for (int i = 0; i < certificates.length; i++) {
            X509Certificate cert = certificates[i];
            log.debug(" Client certificate {}:\n" + "  Subject DN: {}\n" + "  Signature Algorithm: {}\n" + "  Valid from: {}\n" + "  Valid until: {}\n" + "  Issuer: {}", i + 1, cert.getSubjectX500Principal(), cert.getSigAlgName(), cert.getNotBefore(), cert.getNotAfter(), cert.getIssuerX500Principal());
        }
    }
}