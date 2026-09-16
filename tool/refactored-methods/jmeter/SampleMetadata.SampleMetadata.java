/**
 * Construct SampleMetaData from {@link SampleSaveConfiguration}.
 *
 * @param saveConfig
 *            config from which metadata gets extracted (must not be
 *            {@code null})
 */
public SampleMetadata(SampleSaveConfiguration saveConfig) {
    List<String> configuredColumns = new ArrayList<>();
    addColumnIfEnabled(configuredColumns, saveConfig.saveTimestamp(), CSVSaveService.TIME_STAMP);
    addColumnIfEnabled(configuredColumns, saveConfig.saveTime(), CSVSaveService.CSV_ELAPSED);
    addColumnIfEnabled(configuredColumns, saveConfig.saveLabel(), CSVSaveService.LABEL);
    addColumnIfEnabled(configuredColumns, saveConfig.saveCode(), CSVSaveService.RESPONSE_CODE);
    addColumnIfEnabled(configuredColumns, saveConfig.saveMessage(), CSVSaveService.RESPONSE_MESSAGE);
    addColumnIfEnabled(configuredColumns, saveConfig.saveThreadName(), CSVSaveService.THREAD_NAME);
    addColumnIfEnabled(configuredColumns, saveConfig.saveDataType(), CSVSaveService.DATA_TYPE);
    addColumnIfEnabled(configuredColumns, saveConfig.saveSuccess(), CSVSaveService.SUCCESSFUL);
    addColumnIfEnabled(configuredColumns, saveConfig.saveAssertionResultsFailureMessage(), CSVSaveService.FAILURE_MESSAGE);
    addColumnIfEnabled(configuredColumns, saveConfig.saveBytes(), CSVSaveService.CSV_BYTES);
    addColumnIfEnabled(configuredColumns, saveConfig.saveSentBytes(), CSVSaveService.CSV_SENT_BYTES);
    addColumnIfEnabled(configuredColumns, saveConfig.saveThreadCounts(), CSVSaveService.CSV_THREAD_COUNT1);
    addColumnIfEnabled(configuredColumns, saveConfig.saveThreadCounts(), CSVSaveService.CSV_THREAD_COUNT2);
    addColumnIfEnabled(configuredColumns, saveConfig.saveUrl(), CSVSaveService.CSV_URL);
    addColumnIfEnabled(configuredColumns, saveConfig.saveFileName(), CSVSaveService.CSV_FILENAME);
    addColumnIfEnabled(configuredColumns, saveConfig.saveLatency(), CSVSaveService.CSV_LATENCY);
    addColumnIfEnabled(configuredColumns, saveConfig.saveEncoding(), CSVSaveService.CSV_ENCODING);
    addColumnIfEnabled(configuredColumns, saveConfig.saveSampleCount(), CSVSaveService.CSV_SAMPLE_COUNT);
    addColumnIfEnabled(configuredColumns, saveConfig.saveSampleCount(), CSVSaveService.CSV_ERROR_COUNT);
    addColumnIfEnabled(configuredColumns, saveConfig.saveHostname(), CSVSaveService.CSV_HOSTNAME);
    addColumnIfEnabled(configuredColumns, saveConfig.saveIdleTime(), CSVSaveService.CSV_IDLETIME);
    addColumnIfEnabled(configuredColumns, saveConfig.saveConnectTime(), CSVSaveService.CSV_CONNECT_TIME);
    initialize(saveConfig.getDelimiter().charAt(0), configuredColumns);
}
// ---- helper method(s) introduced by the refactoring ----
private static void addColumnIfEnabled(List<String> columns, boolean condition, String columnName) {
    if (condition) {
        columns.add(columnName);
    }
}

