public void cleanExpiredRecordings() {
    log.trace("CleanupJob.cleanExpiredRecordings");
    processExpiringRecordings(true, (rec, days) -> {
        if (days < 0) {
            log.debug("cleanExpiredRecordings:: following recording will be deleted {}", rec);
            File f = rec.getFile(EXTENSION_MP4);
            if (f != null) {
                deleteFile(f);
                recordingDao.delete(rec);
            }
        }
    });
}
// ---- helper method(s) introduced by the refactoring ----
private void deleteFile(File file) {
    try {
        Files.deleteIfExists(file.toPath());
    } catch (IOException err) {
        log.error("Unexpected exception while cleaning up file: {}", file.getAbsolutePath(), err);
    }
}

