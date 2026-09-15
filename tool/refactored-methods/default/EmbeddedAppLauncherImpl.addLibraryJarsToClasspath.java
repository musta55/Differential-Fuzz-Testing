private void addLibraryJarsToClasspath(LogicalPlan lp) throws MalformedURLException {
    String libJarsCsv = lp.getAttributes().get(Context.DAGContext.LIBRARY_JARS);
    if (libJarsCsv == null || libJarsCsv.isEmpty()) {
        return;
    }
    String[] split = libJarsCsv.split(StramClient.LIB_JARS_SEP);
    if (split.length == 0) {
        return;
    }
    URL[] urlList = new URL[split.length];
    int index = 0;
    for (String path : split) {
        File file = new File(path);
        urlList[index++] = file.toURI().toURL();
    }
    ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
    URLClassLoader cl = URLClassLoader.newInstance(urlList, prevCl);
    Thread.currentThread().setContextClassLoader(cl);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean handleLaunchParameters(StramLocalCluster lc, Attribute.AttributeMap launchParameters) throws LauncherException {
    if (launchParameters == null) {
        return false;
    }
    if (StramUtils.getValueWithDefault(launchParameters, SERIALIZE_DAG)) {
        checkDagSerialization();
    }
    lc.setHeartbeatMonitoringEnabled(StramUtils.getValueWithDefault(launchParameters, HEARTBEAT_MONITORING));
    if (StramUtils.getValueWithDefault(launchParameters, RUN_ASYNC)) {
        lc.runAsync();
        return true;
    }
    Long runMillis = StramUtils.getValueWithDefault(launchParameters, RUN_MILLIS);
    if (runMillis != null) {
        lc.run(runMillis);
        return true;
    }
    return false;
}

private void checkDagSerialization() throws LauncherException {
    try {
        cloneDAG();
    } catch (Exception e) {
        throw new LauncherException(e);
    }
}

