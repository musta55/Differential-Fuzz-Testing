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
        String authType = config.get(HADOOP_HTTP_AUTH_PROP);
        if (!HADOOP_HTTP_AUTH_VALUE_SIMPLE.equals(authType)) {
            // turn off authentication for Apex as specified by user
            LOG.warn("Found {} {} but authentication was disabled in Apex.", HADOOP_HTTP_AUTH_PROP, authType);
            config = new Configuration(config);
            config.set(HADOOP_HTTP_AUTH_PROP, HADOOP_HTTP_AUTH_VALUE_SIMPLE);
            config.setBoolean(HADOOP_HTTP_AUTH_SIMPLE_ANONYMOUS_ALLOWED_PROP, true);
        }
    }
    if (sslConfig != null) {
        addSSLConfigResource(config, sslConfig);
    }
    return config;
}