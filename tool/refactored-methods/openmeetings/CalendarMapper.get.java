public MeetingMember get(MeetingMemberDTO dto, User owner) {
    MeetingMember mm = new MeetingMember();
    mm.setId(dto.getId());
    mm.setUser(getUserForMeetingMember(dto, owner));
    return mm;
}
// ---- helper method(s) introduced by the refactoring ----
private Appointment initializeAppointment(AppointmentDTO dto) {
    return dto.getId() == null ? new Appointment() : appointmentDao.get(dto.getId());
}

private void setAppointmentProperties(Appointment a, AppointmentDTO dto, User u) {
    a.setTitle(dto.getTitle());
    a.setLocation(dto.getLocation());
    a.setStart(dto.getStart().getTime());
    a.setEnd(dto.getEnd().getTime());
    a.setDescription(dto.getDescription());
    a.setOwner(getOwner(dto, u));
    a.setInserted(dto.getInserted());
    a.setUpdated(dto.getUpdated());
    a.setDeleted(dto.isDeleted());
    a.setReminder(dto.getReminder());
    a.setRoom(rMapper.get(dto.getRoom()));
    a.setIcalId(dto.getIcalId());
}

private User getOwner(AppointmentDTO dto, User u) {
    return dto.getOwner() == null ? u : userDao.get(dto.getOwner().getId());
}

private List<MeetingMember> processMeetingMembers(AppointmentDTO dto, Appointment a, User u) {
    List<MeetingMember> mml = new ArrayList<>();
    for (MeetingMemberDTO mm : dto.getMeetingMembers()) {
        MeetingMember m = getOrCreateMeetingMember(mm, a, u);
        mml.add(m);
    }
    return mml;
}

private MeetingMember getOrCreateMeetingMember(MeetingMemberDTO mm, Appointment a, User u) {
    MeetingMember m = findExistingMeetingMember(mm, a);
    if (m == null) {
        m = get(mm, u);
        m.setAppointment(a);
    }
    return m;
}

private MeetingMember findExistingMeetingMember(MeetingMemberDTO mm, Appointment a) {
    if (mm.getId() != null) {
        for (MeetingMember m1 : a.getMeetingMembers()) {
            if (m1.getId().equals(mm.getId())) {
                return m1;
            }
        }
        throw new RuntimeException("Weird guest from different appointment is passed");
    }
    return null;
}

private void setAdditionalAppointmentProperties(Appointment a, AppointmentDTO dto) {
    a.setLanguageId(dto.getLanguageId());
    a.setPasswordProtected(dto.isPasswordProtected());
    a.setConnectedEvent(dto.isConnectedEvent());
    a.setReminderEmailSend(dto.isReminderEmailSend());
}

private User getUserForMeetingMember(MeetingMemberDTO dto, User owner) {
    UserDTO user = dto.getUser();
    if (user.getId() != null) {
        return userDao.get(user.getId());
    }
    return retrieveUser(user, owner);
}

private User retrieveUser(UserDTO user, User owner) {
    User u = null;
    if (User.Type.EXTERNAL == user.getType()) {
        u = userDao.getExternalUser(user.getExternalId(), user.getExternalType());
    }
    if (u == null && user.getAddress() != null) {
        u = userDao.getContact(user.getAddress().getEmail(), user.getFirstname(), user.getLastname(), user.getLanguageId(), user.getTimeZoneId(), owner);
    }
    if (u == null) {
        user.setType(User.Type.CONTACT);
        u = uMapper.get(user);
        u.getRights().clear();
    }
    if (Strings.isEmpty(u.getTimeZoneId())) {
        u.setTimeZoneId(owner.getTimeZoneId());
    }
    return u;
}

