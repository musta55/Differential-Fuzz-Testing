@Override
public void stopConsuming() {
    for (int i = 0; i < channelsCount; i++) {
        csvWriters[i].close();
    }
    deleteFolder(getWorkingDirectory());
}
// ---- helper method(s) introduced by the refactoring ----
private static String[] splitFileNameAndExtension(String fileName) {
    int idx = fileName.lastIndexOf('.');
    if (idx >= 0 && idx < fileName.length() - 1) {
        return new String[] { fileName.substring(0, idx), fileName.substring(idx + 1) };
    } else {
        return new String[] { fileName, "" };
    }
}

private static void deleteFolder(File folder) {
    if (!folder.delete()) {
        LOG.warn("Was not able to delete folder {}", folder);
    }
}

