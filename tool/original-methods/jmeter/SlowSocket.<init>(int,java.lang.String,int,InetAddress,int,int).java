public SlowSocket(final int cps, String host, int port, InetAddress localAddress, int localPort, int timeout) throws IOException {
    super();
    if (cps <= 0) {
        throw new IllegalArgumentException("Speed (cps) <= 0");
    }
    charactersPerSecond = cps;
    // This sequence is borrowed from:
    // org.apache.commons.httpclient.protocol.ReflectionSocketFactory.createSocket
    SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
    SocketAddress remoteaddr = new InetSocketAddress(host, port);
    bind(localaddr);
    connect(remoteaddr, timeout);
}