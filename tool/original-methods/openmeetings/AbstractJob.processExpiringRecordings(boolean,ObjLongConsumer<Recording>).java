void processExpiringRecordings(boolean notified, ObjLongConsumer<Recording> consumer) {
    if (!isInitComplete()) {
        return;
    }
    for (Group g : groupDao.getLimited()) {
        for (Recording rec : recordingDao.getExpiring(g.getId(), g.getReminderDays(), notified)) {
            try {
                long days = g.getRecordingTtl() - ChronoUnit.DAYS.between(rec.getInserted().toInstant(), Instant.now());
                consumer.accept(rec, days);
            } catch (Exception e) {
                log.error("Uexpected exception while processing expiring recordings emails", e);
            }
        }
    }
}