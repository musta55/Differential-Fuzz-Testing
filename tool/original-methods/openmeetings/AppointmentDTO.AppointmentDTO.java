public AppointmentDTO(Appointment a) {
    id = a.getId();
    title = a.getTitle();
    location = a.getLocation();
    TimeZone tz = TimezoneUtil.getTimeZone(a.getOwner());
    start = Calendar.getInstance(tz);
    start.setTime(a.getStart());
    end = Calendar.getInstance(tz);
    end.setTime(a.getEnd());
    description = a.getDescription();
    owner = new UserDTO(a.getOwner());
    inserted = a.getInserted();
    updated = a.getUpdated();
    deleted = a.isDeleted();
    reminder = a.getReminder();
    room = new RoomDTO(a.getRoom());
    icalId = a.getIcalId();
    if (a.getMeetingMembers() != null) {
        for (MeetingMember mm : a.getMeetingMembers()) {
            meetingMembers.add(new MeetingMemberDTO(mm));
        }
    }
    languageId = a.getLanguageId();
    passwordProtected = a.isPasswordProtected();
    connectedEvent = a.isConnectedEvent();
    reminderEmailSend = a.isReminderEmailSend();
}