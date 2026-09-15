@Override
public EmbeddedAppHandleImpl launchApp(StreamingApplication application, Configuration configuration, Attribute.AttributeMap launchParameters) throws LauncherException {
    try {
        prepareDAG(application, configuration);
    } catch (Exception e) {
        throw new LauncherException(e);
    }
    StramLocalCluster lc = getController();
    boolean launched = false;
    if (launchParameters != null) {
        if (StramUtils.getValueWithDefault(launchParameters, SERIALIZE_DAG)) {
            // Check if DAG can be serialized
            try {
                cloneDAG();
            } catch (Exception e) {
                throw new LauncherException(e);
            }
        }
        lc.setHeartbeatMonitoringEnabled(StramUtils.getValueWithDefault(launchParameters, HEARTBEAT_MONITORING));
        if (StramUtils.getValueWithDefault(launchParameters, RUN_ASYNC)) {
            lc.runAsync();
            launched = true;
        } else {
            Long runMillis = StramUtils.getValueWithDefault(launchParameters, RUN_MILLIS);
            if (runMillis != null) {
                lc.run(runMillis);
                launched = true;
            }
        }
    }
    if (!launched) {
        lc.run();
    }
    return new EmbeddedAppHandleImpl(lc);
}