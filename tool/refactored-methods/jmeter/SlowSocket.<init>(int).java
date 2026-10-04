/**
 * Added for use by SlowHC4SocketFactory.
 *
 * @param cps characters per second
 */
public SlowSocket(int cps) {
    super();
    validateCharactersPerSecond(cps);
    charactersPerSecond = cps;
}
// ---- helper method(s) introduced by the refactoring ----
private static void validateCharactersPerSecond(int cps) {
    if (cps <= 0) {
        throw new IllegalArgumentException("Speed (cps) <= 0");
    }
}

private void setupSocket(String host, int port, InetAddress localAddress, int localPort, int timeout) throws IOException {
    SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
    SocketAddress remoteaddr = new InetSocketAddress(host, port);
    bind(localaddr);
    connect(remoteaddr, timeout);
}

