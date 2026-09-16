@Override
public int getMessageCount() throws MessagingException {
    if (!isOpen) {
        return -1;
    }
    if (isFile) {
        return 1;
    }
    File[] listFiles = folderPath.listFiles(FILENAME_FILTER);
    return listFiles != null ? listFiles.length : 0;
}