private void sendReminder(User user, Appointment appointment, Invitation invitation) {
    notifierService.notify(user, appointment, invitation);
    if (invitation.getHash() != null) {
        invitationDao.update(invitation);
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

