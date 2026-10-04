@Override
public AppointmentDTO fromString(String val) {
    JSONObject o = new JSONObject(val);
    if (o.has(ROOT)) {
        o = o.getJSONObject(ROOT);
    }
    AppointmentDTO a = new AppointmentDTO();
    setBasicProperties(a, o);
    setDateProperties(a, o);
    setAdditionalProperties(a, o);
    setMeetingMembers(a, o);
    return a;
}
// ---- helper method(s) introduced by the refactoring ----
private void setBasicProperties(AppointmentDTO a, JSONObject o) {
    a.setId(optLong(o, "id"));
    a.setTitle(o.optString("title"));
    a.setLocation(o.optString("location"));
    a.setOwner(UserDTO.get(o.optJSONObject("owner")));
    a.setDescription(o.optString("description"));
    a.setDeleted(o.optBoolean("deleted"));
    a.setReminder(optEnum(Reminder.class, o, "reminder"));
    a.setRoom(RoomDTO.get(o.optJSONObject("room")));
    a.setIcalId(o.optString("icalId"));
    a.setLanguageId(o.optLong("languageId"));
    a.setPassword(o.optString("password"));
    a.setPasswordProtected(o.optBoolean("passwordProtected"));
    a.setConnectedEvent(o.optBoolean("connectedEvent"));
    a.setReminderEmailSend(o.optBoolean("reminderEmailSend"));
}

private void setDateProperties(AppointmentDTO a, JSONObject o) {
    String tzId = a.getOwner() == null ? null : a.getOwner().getTimeZoneId();
    a.setStart(CalendarParamConverter.get(o.optString("start"), tzId));
    a.setEnd(CalendarParamConverter.get(o.optString("end"), tzId));
    a.setInserted(DateParamConverter.get(o.optString("inserted")));
    a.setUpdated(DateParamConverter.get(o.optString("updated")));
}

private void setAdditionalProperties(AppointmentDTO a, JSONObject o) {
    // Additional properties can be set here if needed
}

private void setMeetingMembers(AppointmentDTO a, JSONObject o) {
    JSONArray mm = o.optJSONArray("meetingMembers");
    if (mm != null) {
        for (int i = 0; i < mm.length(); ++i) {
            a.getMeetingMembers().add(MeetingMemberDTO.get(mm.getJSONObject(i)));
        }
    }
}

