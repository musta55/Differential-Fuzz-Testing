private void setMeetingMembers(Appointment a) {
    if (a.getMeetingMembers() != null) {
        for (MeetingMember mm : a.getMeetingMembers()) {
            meetingMembers.add(new MeetingMemberDTO(mm));
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setBasicFields(Appointment a) {
    id = a.getId();
    title = a.getTitle();
    location = a.getLocation();
    description = a.getDescription();
    inserted = a.getInserted();
    updated = a.getUpdated();
    deleted = a.isDeleted();
    icalId = a.getIcalId();
    languageId = a.getLanguageId();
    passwordProtected = a.isPasswordProtected();
    connectedEvent = a.isConnectedEvent();
    reminderEmailSend = a.isReminderEmailSend();
}

private void setDateFields(Appointment a) {
    TimeZone tz = TimezoneUtil.getTimeZone(a.getOwner());
    start = Calendar.getInstance(tz);
    start.setTime(a.getStart());
    end = Calendar.getInstance(tz);
    end.setTime(a.getEnd());
    reminder = a.getReminder();
}

private void setOwnerAndRoom(Appointment a) {
    owner = new UserDTO(a.getOwner());
    room = new RoomDTO(a.getRoom());
}

private void setAdditionalFields(Appointment a) {
    password = a.getPassword();
}

public void setMeetingMembers(List<MeetingMemberDTO> meetingMembers) {
    this.meetingMembers = meetingMembers;
}

