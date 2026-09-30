/**
 * Modify config object by adding SSL related parameters into a resource for WebApp's use
 *
 * @param config  Configuration to be modified
 * @param sslConfig
 */
private static void addSSLConfigResource(Configuration config, SSLConfig sslConfig) {
    String nodeLocalConfig = sslConfig.getConfigPath();
    if (StringUtils.isNotEmpty(nodeLocalConfig)) {
        config.addResource(new Path(nodeLocalConfig));
    } else {
        // create a configuration object and add it as a resource
        Configuration sslConfigResource = new Configuration(false);
        final String SSL_CONFIG_LONG_NAME = Context.DAGContext.SSL_CONFIG.getLongName();
        sslConfigResource.set(SSL_SERVER_KEYSTORE_LOCATION, new Path(sslConfig.getKeyStorePath()).getName(), SSL_CONFIG_LONG_NAME);
        sslConfigResource.set(WebAppUtils.WEB_APP_KEYSTORE_PASSWORD_KEY, sslConfig.getKeyStorePassword(), SSL_CONFIG_LONG_NAME);
        sslConfigResource.set(WebAppUtils.WEB_APP_KEY_PASSWORD_KEY, sslConfig.getKeyStoreKeyPassword(), SSL_CONFIG_LONG_NAME);
        config.addResource(sslConfigResource);
    }
}