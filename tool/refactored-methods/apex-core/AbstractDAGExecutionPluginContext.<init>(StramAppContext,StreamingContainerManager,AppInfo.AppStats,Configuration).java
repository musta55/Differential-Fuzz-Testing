public AbstractDAGExecutionPluginContext(StramAppContext appContext, StreamingContainerManager dnmgr, AppInfo.AppStats stats, Configuration launchConf) {
    this.appContext = appContext;
    this.dnmgr = dnmgr;
    this.launchConf = launchConf;
    this.stats = stats;
}