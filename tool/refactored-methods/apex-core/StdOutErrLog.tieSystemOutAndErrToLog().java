@SuppressWarnings("UseOfSystemOutOrSystemErr")
public static void tieSystemOutAndErrToLog() {
    setupLogging();
    System.setOut(createLoggingProxy(System.out));
    System.setErr(createLoggingProxy(System.err));
}
// ---- helper method(s) introduced by the refactoring ----
private static void setupLogging() {
    org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
    Appender appender = rootLogger.getAppender(DT_LOG_APPENDER);
    if (appender instanceof RollingFileAppender) {
        RollingFileAppender rfa = (RollingFileAppender) appender;
        if (rfa.getFile() == null || rfa.getFile().isEmpty()) {
            rfa.setFile(System.getProperty(DT_LOGDIR));
            rfa.activateOptions();
        }
    } else if (appender != null) {
        logger.warn("found appender {} instead of RollingFileAppender", appender);
    }
    LoggerUtil.addAppenders();
}

