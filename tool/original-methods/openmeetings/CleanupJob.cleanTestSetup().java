public void cleanTestSetup() {
    log.trace("CleanupJob.cleanTestSetup");
    final long now = System.currentTimeMillis();
    if (!isInitComplete()) {
        return;
    }
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
                Files.deleteIfExists(file.toPath());
            }
        }
    } catch (Exception e) {
        log.error("Unexpected exception while processing tests setup videous.", e);
    }
}