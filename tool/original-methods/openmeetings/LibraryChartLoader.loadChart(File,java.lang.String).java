public static List<?> loadChart(File dir, String fileName) {
    try {
        File file = new File(dir, fileName + CHART_EXT);
        log.error("filepathComplete: {}", file);
        XStream xStream = new XStream(new XppDriver());
        xStream.setMode(XStream.NO_REFERENCES);
        try (InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, UTF_8))) {
            return (List<?>) xStream.fromXML(reader);
        }
    } catch (Exception err) {
        log.error("Unexpected error while loading chart", err);
    }
    return List.of();
}