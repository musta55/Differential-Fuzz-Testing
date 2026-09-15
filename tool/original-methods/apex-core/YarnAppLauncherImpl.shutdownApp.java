protected void shutdownApp(YarnAppHandleImpl app, ShutdownMode shutdownMode) throws LauncherException {
    if (shutdownMode == ShutdownMode.KILL) {
        try {
            ApplicationId applicationId = app.appId;
            ApplicationReport appReport = app.yarnClient.getApplicationReport(applicationId);
            if (appReport == null) {
                throw new LauncherException("Application " + app.getApplicationId() + " not found");
            }
            app.yarnClient.killApplication(applicationId);
        } catch (YarnException | IOException e) {
            throw Throwables.propagate(e);
        }
    } else {
        throw new UnsupportedOperationException("Orderly shutdown not supported, try kill instead");
    }
}