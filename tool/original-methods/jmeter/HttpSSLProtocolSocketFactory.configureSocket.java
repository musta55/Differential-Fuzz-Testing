private static void configureSocket(Socket socket) {
    if (!(socket instanceof SSLSocket)) {
        throw new IllegalArgumentException("Expected SSLSocket");
    }
    SSLSocket sock = (SSLSocket) socket;
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