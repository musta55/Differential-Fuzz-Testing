@Override
public int getMessageCount() throws MessagingException {
    if (!isOpen) {
        return -1;
    }
    return isFile ? 1 : countFilesInFolder();
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

