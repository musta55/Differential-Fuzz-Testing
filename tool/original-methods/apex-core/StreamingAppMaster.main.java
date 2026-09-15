/**
 * @param args
 *          Command line args
 * @throws Throwable
 */
public static void main(final String[] args) throws Throwable {
    LoggerUtil.setupMDC("master");
    StdOutErrLog.tieSystemOutAndErrToLog();
    LOG.info("Master starting with classpath: {}", System.getProperty("java.class.path"));
    LOG.info("version: {}", VersionInfo.APEX_VERSION.getBuildVersion());
    StringWriter sw = new StringWriter();
    for (Map.Entry<String, String> e : System.getenv().entrySet()) {
        sw.append("\n").append(e.getKey()).append("=").append(e.getValue());
    }
    LOG.info("appmaster env:" + sw.toString());
    Options opts = new Options();
    opts.addOption("app_attempt_id", true, "App Attempt ID. Not to be used unless for testing purposes");
    opts.addOption("help", false, "Print usage");
    CommandLine cliParser = new GnuParser().parse(opts, args);
    // option "help" overrides and cancels any run
    if (cliParser.hasOption("help")) {
        new HelpFormatter().printHelp("ApplicationMaster", opts);
        return;
    }
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
    boolean result = false;
    StreamingAppMasterService appMaster = null;
    try {
        appMaster = new StreamingAppMasterService(appAttemptID);
        LOG.info("Initializing Application Master.");
        Configuration conf = new YarnConfiguration();
        appMaster.init(conf);
        appMaster.start();
        result = appMaster.run();
    } catch (Throwable t) {
        LOG.error("Exiting Application Master", t);
        System.exit(1);
    } finally {
        if (appMaster != null) {
            appMaster.stop();
        }
    }
    if (result) {
        LOG.info("Application Master completed.");
        System.exit(0);
    } else {
        LOG.info("Application Master failed.");
        System.exit(2);
    }
}