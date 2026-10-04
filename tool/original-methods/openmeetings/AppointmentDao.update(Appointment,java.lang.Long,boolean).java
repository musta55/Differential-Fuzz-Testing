public Appointment update(Appointment a, Long userId, boolean sendmails) {
    Room r = a.getRoom();
    if (r.getId() == null) {
        r.setName(a.getTitle());
        r.setCapacity(cfgDao.getLong(CONFIG_CALENDAR_ROOM_CAPACITY, 50L));
    }
    a.setRoom(roomDao.update(r, userId));
    final boolean newApp = a.getId() == null;
    AppointmentDTO a0 = null;
    Set<Long> mmIds = Set.of();
    if (sendmails && !newApp) {
        Appointment prev = get(a.getId());
        if (prev != null) {
            a0 = new AppointmentDTO(prev);
        }
        mmIds = meetingMemberDao.getMeetingMemberIdsByAppointment(a.getId());
    }
    if (newApp) {
        a.setIcalId(randomUUID().toString());
        em.persist(a);
    } else {
        a = em.merge(a);
    }
    if (sendmails) {
        // update meeting members
        boolean sendMail = a0 == null || !StringUtils.equals(a0.getTitle(), a.getTitle()) || !StringUtils.equals(a0.getDescription(), a.getDescription()) || !StringUtils.equals(a0.getLocation(), a.getLocation()) || !a0.getStart().getTime().equals(a.getStart()) || !a0.getEnd().getTime().equals(a.getEnd());
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
        for (long id : mmIds) {
            invitationManager.processInvitation(a, meetingMemberDao.get(id), MessageType.CANCEL);
        }
        //notify owner
        MeetingMember owner = new MeetingMember();
        owner.setUser(a.getOwner());
        if (newApp) {
            invitationManager.processInvitation(a, owner, MessageType.CREATE);
        } else if (a.isDeleted()) {
            invitationManager.processInvitation(a, owner, MessageType.CANCEL);
        } else if (sendMail) {
            invitationManager.processInvitation(a, owner, MessageType.UPDATE, sendMail);
        }
    }
    return a;
}