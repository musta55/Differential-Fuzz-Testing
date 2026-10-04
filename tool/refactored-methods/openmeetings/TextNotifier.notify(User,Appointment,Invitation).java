@Override
public void notify(User u, Appointment a, Invitation inv) throws Exception {
    if (u.getAddress() == null || Strings.isEmpty(u.getAddress().getPhone())) {
        log.debug("User has no Phone, skip sending notification");
        return;
    }
    final String phone = u.getAddress().getPhone();
    final String reminderMsg = formatReminderMessage(u, a);
    taskExecutor.execute(() -> log.debug("Sending Text to: {}, msg is: {}", phone, reminderMsg));
}
// ---- helper method(s) introduced by the refactoring ----
private String formatReminderMessage(User u, Appointment a) {
    String msg = cfgDao.getString(CONFIG_REMINDER_MESSAGE, null);
    if (Strings.isEmpty(msg)) {
        msg = String.format("%s %s", LabelDao.getString("1158", u.getLanguageId()), a.getTitle());
    }
    return msg;
}

