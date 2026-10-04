public void cleanTestSetup() {
    log.trace("CleanupJob.cleanTestSetup");
    if (!isInitComplete()) {
        return;
    }
    long now = System.currentTimeMillis();
    try {
        File[] folders = getStreamsDir().listFiles(File::isDirectory);
        if (folders == null) {
            return;
        }
        for (File folder : folders) {
            File[] files = folder.listFiles(fi -> fi.getName().startsWith(TEST_SETUP_PREFIX) && fi.isFile() && fi.lastModified() + testSetupTimeout < now);
            if (files == null) {
                continue;
            }
            for (File file : files) {
                log.debug("expired TEST SETUP found: {}", file.getCanonicalPath());
                deleteFile(file);
            }
        }
    } catch (Exception e) {
        log.error("Unexpected exception while processing tests setup videous.", e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void deleteFile(File file) {
    try {
        Files.deleteIfExists(file.toPath());
    } catch (IOException err) {
        log.error("Unexpected exception while cleaning up file: {}", file.getAbsolutePath(), err);
    }
}

