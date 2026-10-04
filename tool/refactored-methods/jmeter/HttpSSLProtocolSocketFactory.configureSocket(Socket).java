private static void configureSocket(Socket socket) {
    if (!(socket instanceof SSLSocket)) {
        throw new IllegalArgumentException("Expected SSLSocket");
    }
    SSLSocket sock = (SSLSocket) socket;
    setEnabledProtocols(sock);
    setEnabledCipherSuites(sock);
}
// ---- helper method(s) introduced by the refactoring ----
private static void setEnabledProtocols(SSLSocket sock) {
    if (!PROTOCOL_LIST.isEmpty()) {
        try {
            sock.setEnabledProtocols(protocols);
        } catch (IllegalArgumentException e) {
            // NOSONAR
            if (log.isWarnEnabled()) {
                log.warn("Could not set protocol list: {}.", PROTOCOL_LIST);
                log.warn("Valid protocols are: {}", join(sock.getSupportedProtocols()));
            }
        }
    }
}

private static void setEnabledCipherSuites(SSLSocket sock) {
    if (!CIPHER_LIST.isEmpty()) {
        try {
            sock.setEnabledCipherSuites(ciphers);
        } catch (IllegalArgumentException e) {
            // NOSONAR
            if (log.isWarnEnabled()) {
                log.warn("Could not set cipher list: {}.", CIPHER_LIST);
                log.warn("Valid ciphers are: {}", join(sock.getSupportedCipherSuites()));
            }
        }
    }
}

