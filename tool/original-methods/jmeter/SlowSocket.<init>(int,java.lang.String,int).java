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
    if (cps <= 0) {
        throw new IllegalArgumentException("Speed (cps) <= 0");
    }
    charactersPerSecond = cps;
}