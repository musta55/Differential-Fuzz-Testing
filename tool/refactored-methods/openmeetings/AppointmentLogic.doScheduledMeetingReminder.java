/**
 * Sending Reminder in Simple mail format 5 minutes before Meeting begins
 */
public void doScheduledMeetingReminder() {
    String baseUrl = getBaseUrl();
    if (baseUrl == null || baseUrl.isEmpty()) {
        log.error("Error retrieving baseUrl for application");
        return;
    }
    int minutesReminderSend = getAppointmentReminderMinutes();
    if (minutesReminderSend == 0) {
        log.warn("minutesReminderSend is 0, disabling reminder scheduler");
        return;
    }
    Calendar start = calculateStart(minutesReminderSend);
    Calendar end = calculateEnd(minutesReminderSend);
    for (Appointment appointment : appointmentDao.getInRange(start, end)) {
        if (appointment.isReminderEmailSend()) {
            continue;
        }
        if (!isTimeToSendReminder(appointment, minutesReminderSend)) {
            continue;
        }
        appointment.setReminderEmailSend(true);
        appointmentDao.update(appointment, null, false);
        List<MeetingMember> members = appointment.getMeetingMembers();
        if (members == null) {
            log.debug("doScheduledMeetingReminder : no members in meeting!");
            continue;
        }
        sendRemindersToMembers(appointment, members);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Invitation createInvitation(User user, Appointment appointment) {
    Invitation invitation = new Invitation();
    invitation.setInvitedBy(user);
    invitation.setInvitee(user);
    invitation.setAppointment(appointment);
    invitation.setRoom(appointment.getRoom());
    return invitation;
}

private Calendar calculateStart(int minutesReminderSend) {
    Calendar start = Calendar.getInstance();
    long milliseconds = minutesReminderSend * 60 * 1000L;
    if (milliseconds < 0) {
        start.setTimeInMillis(start.getTimeInMillis() + milliseconds);
    }
    return start;
}

private Calendar calculateEnd(int minutesReminderSend) {
    Calendar end = Calendar.getInstance();
    long milliseconds = minutesReminderSend * 60 * 1000L;
    if (milliseconds > 0) {
        end.setTimeInMillis(end.getTimeInMillis() + milliseconds);
    }
    return end;
}

private boolean isTimeToSendReminder(Appointment appointment, int minutesReminderSend) {
    TimeZone ownerZone = getTimeZone(appointment.getOwner());
    Calendar now = Calendar.getInstance(ownerZone);
    Calendar start = appointment.startCalendar(ownerZone);
    start.add(Calendar.MINUTE, -minutesReminderSend);
    return !start.after(now);
}

private void sendRemindersToMembers(Appointment appointment, List<MeetingMember> members) {
    sendReminder(appointment.getOwner(), appointment);
    for (MeetingMember member : members) {
        log.debug("doScheduledMeetingReminder : Member {}", member.getUser().getAddress().getEmail());
        Invitation invitation = member.getInvitation();
        sendReminder(member.getUser(), appointment, invitation);
    }
}

