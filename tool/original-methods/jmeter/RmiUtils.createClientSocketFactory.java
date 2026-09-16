public static RMIClientSocketFactory createClientSocketFactory() {
    if (SSL_DISABLED) {
        log.info("Disabling SSL for RMI as server.rmi.ssl.disable is set to 'true'");
        return null;
    }
    if (StringUtils.isBlank(KEYSTORE_FILE)) {
        Validate.validState(SSL_DISABLED, "No keystore for RMI over SSL specified. Set 'server.rmi.ssl.disable' to true, if this is intentional," + "if not run create-rmi-keystore.bat/create-rmi-keystore.sh to create a keystore and distribute it on client and servers" + "used for distributed testing.");
        return null;
    }
    final SSLRMIClientSocketFactory factory = new SSLRMIClientSocketFactory();
    factory.setAlias(KEYSTORE_ALIAS);
    factory.setKeystore(KEYSTORE_FILE, KEYSTORE_TYPE, KEYSTORE_PASSWORD);
    factory.setTruststore(TRUSTSTORE_FILE, TRUSTSTORE_TYPE, TRUSTSTORE_PASSWORD);
    return factory;
}