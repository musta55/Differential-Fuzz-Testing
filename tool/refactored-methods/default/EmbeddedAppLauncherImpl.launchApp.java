@Override
public EmbeddedAppHandleImpl launchApp(StreamingApplication application, Configuration configuration, Attribute.AttributeMap launchParameters) throws LauncherException {
    try {
        prepareDAG(application, configuration);
    } catch (Exception e) {
        throw new LauncherException(e);
    }
    StramLocalCluster lc = getController();
    boolean launched = handleLaunchParameters(lc, launchParameters);
    if (!launched) {
        lc.run();
    }
    return new EmbeddedAppHandleImpl(lc);
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

