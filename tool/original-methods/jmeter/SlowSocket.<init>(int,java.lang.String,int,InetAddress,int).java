/**
 * @param cps
 *            characters per second
 * @param host
 *            hostname
 * @param port
 *            port
 * @param localAddr
 *            local address
 * @param localPort
 *            local port
 *
 * @throws IOException
 *             if an I/O error occurs during initialization
 * @throws IllegalArgumentException
 *             if cps &lt;= 0, or if the <code>port</code> or
 *             <code>localPort</code> values lie outside of the allowed
 *             range between <code>0</code> and <code>65535</code>
 */
public SlowSocket(int cps, String host, int port, InetAddress localAddr, int localPort) throws IOException {
    super(host, port, localAddr, localPort);
    if (cps <= 0) {
        throw new IllegalArgumentException("Speed (cps) <= 0");
    }
    charactersPerSecond = cps;
}