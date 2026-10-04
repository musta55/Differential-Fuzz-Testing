public static RMIServerSocketFactory createServerSocketFactory() throws RemoteException {
    logAndValidateSslDisabled();
    if (StringUtils.isBlank(KEYSTORE_FILE)) {
        validateKeystoreFile();
        return new RMIServerSocketFactoryImpl(getRmiHost());
    }
    return createSslServerSocketFactory();
}
// ---- helper method(s) introduced by the refactoring ----
private static void logAndValidateSslDisabled() {
    if (SSL_DISABLED) {
        log.info("Disabling SSL for RMI as server.rmi.ssl.disable is set to 'true'");
    }
}

private static void validateKeystoreFile() {
    Validate.validState(SSL_DISABLED, "No keystore for RMI over SSL specified. Set 'server.rmi.ssl.disable' to true, if this is intentional," + "if not run create-rmi-keystore.bat/create-rmi-keystore.sh to create a keystore and distribute it on client and servers" + "used for distributed testing.");
}

private static RMIClientSocketFactory createSslClientSocketFactory() {
    final SSLRMIClientSocketFactory factory = new SSLRMIClientSocketFactory();
    factory.setAlias(KEYSTORE_ALIAS);
    factory.setKeystore(KEYSTORE_FILE, KEYSTORE_TYPE, KEYSTORE_PASSWORD);
    factory.setTruststore(TRUSTSTORE_FILE, TRUSTSTORE_TYPE, TRUSTSTORE_PASSWORD);
    return factory;
}

private static RMIServerSocketFactory createSslServerSocketFactory() throws RemoteException {
    SSLRMIServerSocketFactory factory = new SSLRMIServerSocketFactory(getRmiHost());
    factory.setAlias(KEYSTORE_ALIAS);
    factory.setNeedClientAuth(true);
    factory.setKeystore(KEYSTORE_FILE, KEYSTORE_TYPE, KEYSTORE_PASSWORD);
    factory.setTruststore(TRUSTSTORE_FILE, TRUSTSTORE_TYPE, TRUSTSTORE_PASSWORD);
    return factory;
}

private static InetAddress resolveInetAddress() throws RemoteException {
    InetAddress localHost = null;
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
    return localHost;
}

private static void logLocalIpAddress(InetAddress localHost) {
    if (log.isInfoEnabled()) {
        log.info("Local IP address={}", localHost.getHostAddress());
    }
}

private static void validateNonLoopbackAddress(InetAddress localHost) throws RemoteException {
    // $NON-NLS-1$
    String host = System.getProperties().getProperty("java.rmi.server.hostname");
    if (host == null && localHost.isLoopbackAddress()) {
        String hostName = localHost.getHostName();
        throw new RemoteException("Cannot start. " + hostName + " is a loopback address.");
    }
}

