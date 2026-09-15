/**
 * @param args
 *          Command line args
 * @throws Throwable
 */
public static void main(final String[] args) throws Throwable {
    LoggerUtil.setupMDC("master");
    StdOutErrLog.tieSystemOutAndErrToLog();
    logEnvironmentDetails();
    Options opts = createOptions();
    CommandLine cliParser = parseCommandLine(opts, args);
    if (cliParser.hasOption("help")) {
        printHelpMessage(opts);
        return;
    }
    ApplicationAttemptId appAttemptID = getApplicationAttemptId(cliParser);
    StreamingAppMasterService appMaster = null;
    try {
        appMaster = new StreamingAppMasterService(appAttemptID);
        initializeAndStartAppMaster(appMaster);
    } catch (Throwable t) {
        handleException(t, appMaster);
    } finally {
        stopAppMaster(appMaster);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void logEnvironmentDetails() {
    LOG.info("Master starting with classpath: {}", System.getProperty("java.class.path"));
    LOG.info("version: {}", VersionInfo.APEX_VERSION.getBuildVersion());
    StringWriter sw = new StringWriter();
    for (Map.Entry<String, String> e : System.getenv().entrySet()) {
        sw.append("\n").append(e.getKey()).append("=").append(e.getValue());
    }
    LOG.info("appmaster env:" + sw.toString());
}

private static Options createOptions() {
    Options opts = new Options();
    opts.addOption("app_attempt_id", true, "App Attempt ID. Not to be used unless for testing purposes");
    opts.addOption("help", false, "Print usage");
    return opts;
}

private static CommandLine parseCommandLine(Options opts, String[] args) throws Throwable {
    return new GnuParser().parse(opts, args);
}

private static void printHelpMessage(Options opts) {
    new HelpFormatter().printHelp("ApplicationMaster", opts);
}

private static ApplicationAttemptId getApplicationAttemptId(CommandLine cliParser) {
    Map<String, String> envs = System.getenv();
    ApplicationAttemptId appAttemptID = Records.newRecord(ApplicationAttemptId.class);
    if (!envs.containsKey(Environment.CONTAINER_ID.name())) {
        if (cliParser.hasOption("app_attempt_id")) {
            String appIdStr = cliParser.getOptionValue("app_attempt_id", "");
            appAttemptID = ConverterUtils.toApplicationAttemptId(appIdStr);
        } else {
            throw new IllegalArgumentException("Application Attempt Id not set in the environment");
        }
    } else {
        ContainerId containerId = ConverterUtils.toContainerId(envs.get(Environment.CONTAINER_ID.name()));
        appAttemptID = containerId.getApplicationAttemptId();
    }
    return appAttemptID;
}

private static void initializeAndStartAppMaster(StreamingAppMasterService appMaster) throws Throwable {
    LOG.info("Initializing Application Master.");
    Configuration conf = new YarnConfiguration();
    appMaster.init(conf);
    appMaster.start();
    boolean result = appMaster.run();
    if (result) {
        LOG.info("Application Master completed.");
        System.exit(0);
    } else {
        LOG.info("Application Master failed.");
        System.exit(2);
    }
}

private static void handleException(Throwable t, StreamingAppMasterService appMaster) {
    LOG.error("Exiting Application Master", t);
    System.exit(1);
}

private static void stopAppMaster(StreamingAppMasterService appMaster) {
    if (appMaster != null) {
        appMaster.stop();
    }
}

