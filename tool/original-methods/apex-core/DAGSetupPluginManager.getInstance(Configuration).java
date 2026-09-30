public static synchronized DAGSetupPluginManager getInstance(Configuration conf) {
    DAGSetupPluginManager manager = new DAGSetupPluginManager(conf);
    return manager;
}