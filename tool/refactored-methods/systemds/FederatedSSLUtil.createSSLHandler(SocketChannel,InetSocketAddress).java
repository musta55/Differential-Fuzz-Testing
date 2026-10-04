protected static SslHandler createSSLHandler(SocketChannel ch, InetSocketAddress address) {
    return getSslContextMan().getContext().newHandler(ch.alloc(), address.getAddress().getHostAddress(), address.getPort());
}
// ---- helper method(s) introduced by the refactoring ----
protected static SslContextMan getSslContextMan() {
    if (sslInstance == null) {
        sslInstance = new SslContextMan();
    }
    return sslInstance;
}

