public static synchronized DAGSetupPluginManager getInstance(Configuration conf) {
    return new DAGSetupPluginManager(conf);
}