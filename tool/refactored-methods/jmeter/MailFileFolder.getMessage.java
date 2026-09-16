@Override
public Message getMessage(int index) throws MessagingException {
    File file = getFileForIndex(index);
    try (InputStream fis = new FileInputStream(file);
        InputStream bis = new BufferedInputStream(fis)) {
        return new MailFileMessage(this, bis, index);
    } catch (IOException e) {
        throw new MessagingException("Cannot open folder: " + e.getMessage(), e);
    }
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

