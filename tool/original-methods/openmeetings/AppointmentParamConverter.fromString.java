@Override
public AppointmentDTO fromString(String val) {
    JSONObject o = new JSONObject(val);
    if (o.has(ROOT)) {
        o = o.getJSONObject(ROOT);
    }
    AppointmentDTO a = new AppointmentDTO();
    a.setId(optLong(o, "id"));
    a.setTitle(o.optString("title"));
    a.setLocation(o.optString("location"));
    a.setOwner(UserDTO.get(o.optJSONObject("owner")));
    String tzId = a.getOwner() == null ? null : a.getOwner().getTimeZoneId();
    a.setStart(CalendarParamConverter.get(o.optString("start"), tzId));
    a.setEnd(CalendarParamConverter.get(o.optString("end"), tzId));
    a.setDescription(o.optString("description"));
    a.setInserted(DateParamConverter.get(o.optString("inserted")));
    a.setUpdated(DateParamConverter.get(o.optString("updated")));
    a.setDeleted(o.optBoolean("deleted"));
    a.setReminder(optEnum(Reminder.class, o, "reminder"));
    a.setRoom(RoomDTO.get(o.optJSONObject("room")));
    a.setIcalId(o.optString("icalId"));
    JSONArray mm = o.optJSONArray("meetingMembers");
    if (mm != null) {
        for (int i = 0; i < mm.length(); ++i) {
            a.getMeetingMembers().add(MeetingMemberDTO.get(mm.getJSONObject(i)));
        }
    }
    a.setLanguageId(o.optLong("languageId"));
    a.setPassword(o.optString("password"));
    a.setPasswordProtected(o.optBoolean("passwordProtected"));
    a.setConnectedEvent(o.optBoolean("connectedEvent"));
    a.setReminderEmailSend(o.optBoolean("reminderEmailSend"));
    return a;
}