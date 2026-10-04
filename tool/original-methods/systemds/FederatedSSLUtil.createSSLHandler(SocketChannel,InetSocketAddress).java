protected static SslHandler createSSLHandler(SocketChannel ch, InetSocketAddress address) {
    return SslConstructor().context.newHandler(ch.alloc(), address.getAddress().getHostAddress(), address.getPort());
}