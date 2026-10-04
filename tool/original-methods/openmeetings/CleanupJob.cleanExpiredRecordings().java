public void cleanExpiredRecordings() {
    log.trace("CleanupJob.cleanExpiredRecordings");
    processExpiringRecordings(true, (rec, days) -> {
        if (days < 0) {
            log.debug("cleanExpiredRecordings:: following recording will be deleted {}", rec);
            File f = rec.getFile(EXTENSION_MP4);
            if (f != null) {
                try {
                    Files.deleteIfExists(f.toPath());
                } catch (IOException err) {
                    log.error("Unexpected exception while clen-up expired recording", err);
                }
            }
            recordingDao.delete(rec);
        }
    });
}