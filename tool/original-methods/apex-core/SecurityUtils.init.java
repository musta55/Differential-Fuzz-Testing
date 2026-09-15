public static void init(Configuration configuration, StramHTTPAuthentication stramHTTPAuth) {
    hadoopWebSecurityEnabled = false;
    String authValue = configuration.get(HADOOP_HTTP_AUTH_PROP);
    if ((authValue != null) && !authValue.equals(HADOOP_HTTP_AUTH_VALUE_SIMPLE)) {
        hadoopWebSecurityEnabled = true;
        initAuth(configuration);
    }
    // Stram http auth may not be specified and is null but still set a default
    if (stramHTTPAuth == StramHTTPAuthentication.FOLLOW_HADOOP_HTTP_AUTH) {
        stramWebSecurityEnabled = hadoopWebSecurityEnabled;
    } else if (stramHTTPAuth == StramHTTPAuthentication.ENABLE) {
        stramWebSecurityEnabled = true;
    } else if (stramHTTPAuth == StramHTTPAuthentication.DISABLE) {
        stramWebSecurityEnabled = false;
    } else {
        // Default to StramHTTPAuthentication.FOLLOW_HADOOP_AUTH behavior
        stramWebSecurityEnabled = UserGroupInformation.isSecurityEnabled();
    }
}