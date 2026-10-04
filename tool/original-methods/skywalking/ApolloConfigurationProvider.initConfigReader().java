@Override
protected ConfigWatcherRegister initConfigReader() throws ModuleStartException {
    final String apolloCluster = settings.getApolloCluster();
    if (!Strings.isNullOrEmpty(apolloCluster)) {
        System.setProperty("apollo.cluster", apolloCluster);
    }
    final String apolloMeta = settings.getApolloMeta();
    if (!Strings.isNullOrEmpty(apolloMeta)) {
        System.setProperty("apollo.meta", apolloMeta);
    }
    final String appId = settings.getAppId();
    if (!Strings.isNullOrEmpty(appId)) {
        System.setProperty("app.id", appId);
    }
    final String env = settings.getApolloEnv();
    if (!Strings.isNullOrEmpty(env)) {
        System.setProperty("env", env);
    }
    return new ApolloConfigWatcherRegister(settings);
}