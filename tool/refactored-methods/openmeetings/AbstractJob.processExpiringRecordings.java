void processExpiringRecordings(boolean notified, ObjLongConsumer<Recording> consumer) {
    if (!isInitComplete()) {
        return;
    }
    for (Group g : groupDao.getLimited()) {
        processRecordingsForGroup(g, notified, consumer);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void processRecordingsForGroup(Group g, boolean notified, ObjLongConsumer<Recording> consumer) {
    for (Recording rec : recordingDao.getExpiring(g.getId(), g.getReminderDays(), notified)) {
        processRecording(g, rec, consumer);
    }
}

private void processRecording(Group g, Recording rec, ObjLongConsumer<Recording> consumer) {
    try {
        long days = calculateRemainingDays(g, rec);
        consumer.accept(rec, days);
    } catch (Exception e) {
        log.error("Unexpected exception while processing expiring recordings emails", e);
    }
}

private long calculateRemainingDays(Group g, Recording rec) {
    return g.getRecordingTtl() - ChronoUnit.DAYS.between(rec.getInserted().toInstant(), Instant.now());
}

