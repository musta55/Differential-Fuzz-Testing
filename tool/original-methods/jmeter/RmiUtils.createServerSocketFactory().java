public static RMIServerSocketFactory createServerSocketFactory() throws RemoteException {
    if (SSL_DISABLED) {
        log.info("Disabling SSL for RMI as server.rmi.ssl.disable is set to 'true'");
        return null;
    }
    if (StringUtils.isBlank(KEYSTORE_FILE)) {
        Validate.validState(SSL_DISABLED, "No keystore for RMI over SSL specified. Set 'server.rmi.ssl.disable' to true, if this is intentional.");
        return new RMIServerSocketFactoryImpl(getRmiHost());
    }
    SSLRMIServerSocketFactory factory = new SSLRMIServerSocketFactory(getRmiHost());
    factory.setAlias(KEYSTORE_ALIAS);
    factory.setNeedClientAuth(true);
    factory.setKeystore(KEYSTORE_FILE, KEYSTORE_TYPE, KEYSTORE_PASSWORD);
    factory.setTruststore(TRUSTSTORE_FILE, TRUSTSTORE_TYPE, TRUSTSTORE_PASSWORD);
    return factory;
}