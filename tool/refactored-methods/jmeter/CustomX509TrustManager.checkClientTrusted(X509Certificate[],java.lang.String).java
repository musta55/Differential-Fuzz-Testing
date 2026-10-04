/**
 * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String)
 */
@Override
public void checkClientTrusted(X509Certificate[] certificates, String authType) {
    // NOSONAR JMeter is a pentest and perf testing tool
    logCertificates(certificates, "Client");
}
// ---- helper method(s) introduced by the refactoring ----
private static void logCertificates(X509Certificate[] certificates, String type) {
    if (log.isDebugEnabled() && certificates != null) {
        for (int i = 0; i < certificates.length; i++) {
            X509Certificate cert = certificates[i];
            log.debug(" {} certificate {}:\n" + "  Subject DN: {}\n" + "  Signature Algorithm: {}\n" + "  Valid from: {}\n" + "  Valid until: {}\n" + "  Issuer: {}", type, i + 1, cert.getSubjectX500Principal(), cert.getSigAlgName(), cert.getNotBefore(), cert.getNotAfter(), cert.getIssuerX500Principal());
        }
    }
}

