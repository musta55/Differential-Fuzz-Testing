/*
     * called by CSVRead(file,alias)
     */
public static synchronized void open(String file, String alias) {
    log.info("Opening {} as {}", file, alias);
    file = checkDefault(file);
    if (alias.length() == 0) {
        log.error("Alias cannot be empty");
        return;
    }
    Map<String, FileWrapper> m = filePacks.get();
    if (m.get(alias) == null) {
        try {
            FileRowColContainer frcc = getFile(file, alias);
            log.info("Stored {} as {}", file, alias);
            m.put(alias, new FileWrapper(frcc));
        } catch (IOException e) {
            // Already logged
        }
    }
}