public Appointment get(AppointmentDTO dto, User u) {
    Appointment a = dto.getId() == null ? new Appointment() : appointmentDao.get(dto.getId());
    a.setId(dto.getId());
    a.setTitle(dto.getTitle());
    a.setLocation(dto.getLocation());
    a.setStart(dto.getStart().getTime());
    a.setEnd(dto.getEnd().getTime());
    a.setDescription(dto.getDescription());
    a.setOwner(dto.getOwner() == null ? u : userDao.get(dto.getOwner().getId()));
    a.setInserted(dto.getInserted());
    a.setUpdated(dto.getUpdated());
    a.setDeleted(dto.isDeleted());
    a.setReminder(dto.getReminder());
    a.setRoom(rMapper.get(dto.getRoom()));
    a.setIcalId(dto.getIcalId());
    List<MeetingMember> mml = new ArrayList<>();
    for (MeetingMemberDTO mm : dto.getMeetingMembers()) {
        MeetingMember m = null;
        if (mm.getId() != null) {
            //if ID is NOT null it should already be in the list
            for (MeetingMember m1 : a.getMeetingMembers()) {
                if (m1.getId().equals(mm.getId())) {
                    m = m1;
                    break;
                }
            }
            if (m == null) {
                throw new RuntimeException("Weird guest from different appointment is passed");
            }
        } else {
            m = get(mm, u);
            m.setAppointment(a);
        }
        mml.add(m);
    }
    a.setMeetingMembers(mml);
    a.setLanguageId(dto.getLanguageId());
    a.setPasswordProtected(dto.isPasswordProtected());
    a.setConnectedEvent(dto.isConnectedEvent());
    a.setReminderEmailSend(dto.isReminderEmailSend());
    return a;
}