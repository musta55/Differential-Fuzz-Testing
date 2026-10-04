/**
 * Construct SampleMetaData from {@link SampleSaveConfiguration}.
 *
 * @param saveConfig
 *            config from which metadata gets extracted (must not be
 *            {@code null})
 */
public SampleMetadata(SampleSaveConfiguration saveConfig) {
    List<String> configuredColumns = new ArrayList<>();
    if (saveConfig.saveTimestamp()) {
        configuredColumns.add(CSVSaveService.TIME_STAMP);
    }
    if (saveConfig.saveTime()) {
        configuredColumns.add(CSVSaveService.CSV_ELAPSED);
    }
    if (saveConfig.saveLabel()) {
        configuredColumns.add(CSVSaveService.LABEL);
    }
    if (saveConfig.saveCode()) {
        configuredColumns.add(CSVSaveService.RESPONSE_CODE);
    }
    if (saveConfig.saveMessage()) {
        configuredColumns.add(CSVSaveService.RESPONSE_MESSAGE);
    }
    if (saveConfig.saveThreadName()) {
        configuredColumns.add(CSVSaveService.THREAD_NAME);
    }
    if (saveConfig.saveDataType()) {
        configuredColumns.add(CSVSaveService.DATA_TYPE);
    }
    if (saveConfig.saveSuccess()) {
        configuredColumns.add(CSVSaveService.SUCCESSFUL);
    }
    if (saveConfig.saveAssertionResultsFailureMessage()) {
        configuredColumns.add(CSVSaveService.FAILURE_MESSAGE);
    }
    if (saveConfig.saveBytes()) {
        configuredColumns.add(CSVSaveService.CSV_BYTES);
    }
    if (saveConfig.saveSentBytes()) {
        configuredColumns.add(CSVSaveService.CSV_SENT_BYTES);
    }
    if (saveConfig.saveThreadCounts()) {
        configuredColumns.add(CSVSaveService.CSV_THREAD_COUNT1);
        configuredColumns.add(CSVSaveService.CSV_THREAD_COUNT2);
    }
    if (saveConfig.saveUrl()) {
        configuredColumns.add(CSVSaveService.CSV_URL);
    }
    if (saveConfig.saveFileName()) {
        configuredColumns.add(CSVSaveService.CSV_FILENAME);
    }
    if (saveConfig.saveLatency()) {
        configuredColumns.add(CSVSaveService.CSV_LATENCY);
    }
    if (saveConfig.saveEncoding()) {
        configuredColumns.add(CSVSaveService.CSV_ENCODING);
    }
    if (saveConfig.saveSampleCount()) {
        configuredColumns.add(CSVSaveService.CSV_SAMPLE_COUNT);
        configuredColumns.add(CSVSaveService.CSV_ERROR_COUNT);
    }
    if (saveConfig.saveHostname()) {
        configuredColumns.add(CSVSaveService.CSV_HOSTNAME);
    }
    if (saveConfig.saveIdleTime()) {
        configuredColumns.add(CSVSaveService.CSV_IDLETIME);
    }
    if (saveConfig.saveConnectTime()) {
        configuredColumns.add(CSVSaveService.CSV_CONNECT_TIME);
    }
    initialize(saveConfig.getDelimiter().charAt(0), configuredColumns);
}