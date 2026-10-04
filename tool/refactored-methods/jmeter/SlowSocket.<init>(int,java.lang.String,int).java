/**
 * @param cps
 *            characters per second
 * @param host
 *            hostname
 * @param port
 *            port
 *
 * @throws UnknownHostException
 *             if the name of the host can not be determined automatically
 * @throws IOException
 *             if an I/O error occurs during initialization
 * @throws IllegalArgumentException
 *             if cps &lt;= 0, or if the <code>port</code> or
 *             <code>localPort</code> values lie outside of the allowed
 *             range between <code>0</code> and <code>65535</code>
 */
public SlowSocket(int cps, String host, int port) throws UnknownHostException, IOException {
    super(host, port);
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

