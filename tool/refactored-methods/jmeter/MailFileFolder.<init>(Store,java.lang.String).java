public MailFileFolder(Store store, String path) {
    super(store);
    // == ServerName from mail sampler
    String base = store.getURLName().getHost();
    File parentFolder = new File(base);
    isFile = parentFolder.isFile();
    folderPath = isFile ? parentFolder : new File(base, path);
}
// ---- helper method(s) introduced by the refactoring ----
private File getFileForIndex(int index) {
    return isFile ? folderPath : new File(folderPath, String.format("%d.msg", index));
}

private int countFilesInFolder() {
    File[] listFiles = folderPath.listFiles(FILENAME_FILTER);
    return listFiles != null ? listFiles.length : 0;
}

private static void validateMode(int mode) throws MessagingException {
    if (mode != READ_ONLY) {
        throw new MessagingException("Implementation only supports read-only access");
    }
}

