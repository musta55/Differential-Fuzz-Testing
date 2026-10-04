/**
 * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String)
 */
@Override
public // NOSONAR JMeter is a pentest and perf testing tool
void // NOSONAR JMeter is a pentest and perf testing tool
checkServerTrusted(// NOSONAR JMeter is a pentest and perf testing tool
X509Certificate[] certificates, // NOSONAR JMeter is a pentest and perf testing tool
String authType) throws CertificateException {
    if (log.isDebugEnabled() && certificates != null) {
        for (int i = 0; i < certificates.length; i++) {
            X509Certificate cert = certificates[i];
            log.debug(" Server certificate {}:\n" + "  Subject DN: {}\n" + "  Signature Algorithm: {}\n" + "  Valid from: {}\n" + "  Valid until: {}\n" + "  Issuer: {}", i + 1, cert.getSubjectX500Principal(), cert.getSigAlgName(), cert.getNotBefore(), cert.getNotAfter(), cert.getIssuerX500Principal());
        }
    }
}