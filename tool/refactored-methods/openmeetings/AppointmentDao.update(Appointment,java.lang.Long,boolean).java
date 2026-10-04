public Appointment update(Appointment a, Long userId, boolean sendmails) {
    updateRoom(a, userId);
    final boolean newApp = a.getId() == null;
    AppointmentDTO a0 = null;
    Set<Long> mmIds = Set.of();
    if (sendmails && !newApp) {
        a0 = new AppointmentDTO(get(a.getId()));
        mmIds = meetingMemberDao.getMeetingMemberIdsByAppointment(a.getId());
    }
    persistOrMergeAppointment(a, newApp);
    if (sendmails) {
        updateMeetingMembers(a, a0, mmIds);
        notifyOwner(a, newApp, a0);
    }
    return a;
}
// ---- helper method(s) introduced by the refactoring ----
private void updateRoom(Appointment a, Long userId) {
    Room r = a.getRoom();
    if (r.getId() == null) {
        r.setName(a.getTitle());
        r.setCapacity(cfgDao.getLong(CONFIG_CALENDAR_ROOM_CAPACITY, 50L));
    }
    a.setRoom(roomDao.update(r, userId));
}

private void persistOrMergeAppointment(Appointment a, boolean newApp) {
    if (newApp) {
        a.setIcalId(randomUUID().toString());
        em.persist(a);
    } else {
        em.merge(a);
    }
}

private void updateMeetingMembers(Appointment a, AppointmentDTO a0, Set<Long> mmIds) {
    boolean sendMail = shouldSendMail(a, a0);
    List<MeetingMember> mmList = a.getMeetingMembers();
    if (mmList != null) {
        for (MeetingMember mm : mmList) {
            if (mm.getId() == null || !mmIds.contains(mm.getId())) {
                invitationManager.processInvitation(a, mm, MessageType.CREATE);
            } else {
                mmIds.remove(mm.getId());
                invitationManager.processInvitation(a, mm, MessageType.UPDATE, sendMail);
            }
        }
    }
    for (Long id : mmIds) {
        invitationManager.processInvitation(a, meetingMemberDao.get(id), MessageType.CANCEL);
    }
}

private boolean shouldSendMail(Appointment a, AppointmentDTO a0) {
    return a0 == null || !StringUtils.equals(a0.getTitle(), a.getTitle()) || !StringUtils.equals(a0.getDescription(), a.getDescription()) || !StringUtils.equals(a0.getLocation(), a.getLocation()) || !a0.getStart().getTime().equals(a.getStart()) || !a0.getEnd().getTime().equals(a.getEnd());
}

private void notifyOwner(Appointment a, boolean newApp, AppointmentDTO a0) {
    MeetingMember owner = new MeetingMember();
    owner.setUser(a.getOwner());
    if (newApp) {
        invitationManager.processInvitation(a, owner, MessageType.CREATE);
    } else if (a.isDeleted()) {
        invitationManager.processInvitation(a, owner, MessageType.CANCEL);
    } else if (shouldSendMail(a, a0)) {
        invitationManager.processInvitation(a, owner, MessageType.UPDATE, true);
    }
}

