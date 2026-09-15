public AbstractDAGExecutionPluginContext(StramAppContext appContext, StreamingContainerManager dnmgr, AppInfo.AppStats stats, Configuration launcConf) {
    this.appContext = appContext;
    this.dnmgr = dnmgr;
    this.launchConf = launcConf;
    this.stats = stats;
}