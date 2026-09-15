/**
 * Setup security related configuration for {@link org.apache.hadoop.yarn.webapp.WebApp}.
 * @param config
 * @param sslConfig
 * @return
 */
public static Configuration configureWebAppSecurity(Configuration config, SSLConfig sslConfig) {
    if (isStramWebSecurityEnabled()) {
        config = new Configuration(config);
        config.set("hadoop.http.filter.initializers", StramWSFilterInitializer.class.getCanonicalName());
    } else {
        handleNonSimpleAuth(config);
    }
    if (sslConfig != null) {
        addSSLConfigResource(config, sslConfig);
    }
    return config;
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isNonSimpleAuth(String authValue) {
    return authValue != null && !authValue.equals(HADOOP_HTTP_AUTH_VALUE_SIMPLE);
}

private static void setStramWebSecurityEnabled(StramHTTPAuthentication stramHTTPAuth) {
    if (stramHTTPAuth == StramHTTPAuthentication.FOLLOW_HADOOP_HTTP_AUTH) {
        stramWebSecurityEnabled = hadoopWebSecurityEnabled;
    } else if (stramHTTPAuth == StramHTTPAuthentication.ENABLE) {
        stramWebSecurityEnabled = true;
    } else if (stramHTTPAuth == StramHTTPAuthentication.DISABLE) {
        stramWebSecurityEnabled = false;
    } else {
        stramWebSecurityEnabled = UserGroupInformation.isSecurityEnabled();
    }
}

private static void handleNonSimpleAuth(Configuration config) {
    String authType = config.get(HADOOP_HTTP_AUTH_PROP);
    if (!HADOOP_HTTP_AUTH_VALUE_SIMPLE.equals(authType)) {
        LOG.warn("Found {} {} but authentication was disabled in Apex.", HADOOP_HTTP_AUTH_PROP, authType);
        config = new Configuration(config);
        config.set(HADOOP_HTTP_AUTH_PROP, HADOOP_HTTP_AUTH_VALUE_SIMPLE);
        config.setBoolean(HADOOP_HTTP_AUTH_SIMPLE_ANONYMOUS_ALLOWED_PROP, true);
    }
}

private static Configuration createSSLConfigResource(SSLConfig sslConfig) {
    Configuration sslConfigResource = new Configuration(false);
    final String SSL_CONFIG_LONG_NAME = Context.DAGContext.SSL_CONFIG.getLongName();
    sslConfigResource.set(SSL_SERVER_KEYSTORE_LOCATION, new Path(sslConfig.getKeyStorePath()).getName(), SSL_CONFIG_LONG_NAME);
    sslConfigResource.set(WebAppUtils.WEB_APP_KEYSTORE_PASSWORD_KEY, sslConfig.getKeyStorePassword(), SSL_CONFIG_LONG_NAME);
    sslConfigResource.set(WebAppUtils.WEB_APP_KEY_PASSWORD_KEY, sslConfig.getKeyStoreKeyPassword(), SSL_CONFIG_LONG_NAME);
    return sslConfigResource;
}

