public static List<?> loadChart(File dir, String fileName) {
    File file = new File(dir, fileName + CHART_EXT);
    logFilePath(file);
    try (InputStream is = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, UTF_8))) {
        return parseChartFile(reader);
    } catch (Exception err) {
        handleException(err);
    }
    return List.of();
}
// ---- helper method(s) introduced by the refactoring ----
private static void logFilePath(File file) {
    log.error("filepathComplete: {}", file);
}

private static List<?> parseChartFile(BufferedReader reader) throws Exception {
    XStream xStream = new XStream(new XppDriver());
    xStream.setMode(XStream.NO_REFERENCES);
    return (List<?>) xStream.fromXML(reader);
}

private static void handleException(Exception err) {
    log.error("Unexpected error while loading chart", err);
}

