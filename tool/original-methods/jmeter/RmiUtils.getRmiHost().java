/**
 * @return the configure address for usage by RMI
 * @throws RemoteException when no valid address can be found
 */
public static InetAddress getRmiHost() throws RemoteException {
    InetAddress localHost = null;
    // Bug 47980 - allow override of local hostname
    // $NON-NLS-1$
    String host = System.getProperties().getProperty("java.rmi.server.hostname");
    try {
        if (host == null) {
            log.info("System property 'java.rmi.server.hostname' is not defined, using localHost address");
            localHost = InetAddress.getLocalHost();
        } else {
            log.info("Resolving by name the value of System property 'java.rmi.server.hostname': {}", host);
            localHost = InetAddress.getByName(host);
        }
    } catch (UnknownHostException e) {
        throw new RemoteException("Cannot start. Unable to get local host IP address.", e);
    }
    if (log.isInfoEnabled()) {
        log.info("Local IP address={}", localHost.getHostAddress());
    }
    // BUG 52469 : Allow loopback address for SSH Tunneling of RMI traffic
    if (host == null && localHost.isLoopbackAddress()) {
        String hostName = localHost.getHostName();
        throw new RemoteException("Cannot start. " + hostName + " is a loopback address.");
    }
    return localHost;
}