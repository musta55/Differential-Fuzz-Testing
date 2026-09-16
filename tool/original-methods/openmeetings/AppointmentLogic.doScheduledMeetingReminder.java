/**
 * Sending Reminder in Simple mail format 5 minutes before Meeting begins
 */
// ----------------------------------------------------------------------------------------------
public void doScheduledMeetingReminder() {
    String baseUrl = getBaseUrl();
    if (baseUrl == null || baseUrl.length() < 1) {
        log.error("Error retrieving baseUrl for application");
        return;
    }
    int minutesReminderSend = getAppointmentReminderMinutes();
    if (minutesReminderSend == 0) {
        log.warn("minutesReminderSend is 0, disabling reminder scheduler");
        return;
    }
    long milliseconds = minutesReminderSend * 60 * 1000L;
    Calendar start = Calendar.getInstance();
    if (milliseconds < 0) {
        start.setTimeInMillis(start.getTimeInMillis() + milliseconds);
    }
    Calendar end = Calendar.getInstance();
    if (milliseconds > 0) {
        end.setTimeInMillis(end.getTimeInMillis() + milliseconds);
    }
    for (Appointment a : appointmentDao.getInRange(start, end)) {
        // Prevent email from being send twice, even if the cycle takes
        // very long to send each
        if (a.isReminderEmailSend()) {
            continue;
        }
        TimeZone ownerZone = getTimeZone(a.getOwner());
        Calendar aNow = Calendar.getInstance(ownerZone);
        Calendar aStart = a.startCalendar(ownerZone);
        aStart.add(Calendar.MINUTE, -minutesReminderSend);
        if (aStart.after(aNow)) {
            // to early to send reminder
            continue;
        }
        // Update Appointment to not send invitation twice
        a.setReminderEmailSend(true);
        appointmentDao.update(a, null, false);
        List<MeetingMember> members = a.getMeetingMembers();
        sendReminder(a.getOwner(), a);
        if (members == null) {
            log.debug("doScheduledMeetingReminder : no members in meeting!");
            continue;
        }
        // Iterate through all MeetingMembers
        for (MeetingMember mm : members) {
            log.debug("doScheduledMeetingReminder : Member {}", mm.getUser().getAddress().getEmail());
            Invitation inv = mm.getInvitation();
            sendReminder(mm.getUser(), a, inv);
        }
    }
}