public Invitation get(InvitationDTO dto, Long userId) {
    Invitation i = new Invitation();
    setInvitationBasicProperties(i, dto);
    setInvitationValidity(i, dto);
    setInvitationUserDetails(i, dto, userId);
    setInvitationRoom(i, dto);
    return i;
}
// ---- helper method(s) introduced by the refactoring ----
private Room initializeRoom(RoomDTO dto) {
    return dto.getId() == null ? new Room() : roomDao.get(dto.getId());
}

private void setRoomProperties(Room r, RoomDTO dto) {
    r.setId(dto.getId());
    r.setName(dto.getName());
    r.setTag(dto.getTag());
    r.setComment(dto.getComment());
    r.setType(dto.getType());
    r.setCapacity(dto.getCapacity());
    r.setAppointment(dto.isAppointment());
    r.setConfno(dto.getConfno());
    r.setIspublic(dto.isPublic());
    r.setDemoRoom(dto.isDemo());
    r.setClosed(dto.isClosed());
    r.setDemoTime(dto.getDemoTime());
    r.setExternalId(dto.getExternalId());
}

private void handleExternalGroup(Room r, RoomDTO dto) {
    String externalType = dto.getExternalType();
    if (!Strings.isEmpty(externalType) && !hasExternalGroup(r, externalType)) {
        r.addGroup(groupDao.getExternal(externalType));
    }
}

private boolean hasExternalGroup(Room r, String externalType) {
    return r.getGroups().stream().anyMatch(gu -> gu.getGroup().isExternal() && gu.getGroup().getName().equals(externalType));
}

private void setAdditionalRoomProperties(Room r, RoomDTO dto) {
    r.setRedirectURL(dto.getRedirectUrl());
    r.setModerated(dto.isModerated());
    r.setWaitModerator(dto.isWaitModerator());
    r.setAllowUserQuestions(dto.isAllowUserQuestions());
    r.setAllowRecording(dto.isAllowRecording());
    r.setWaitRecording(dto.isWaitRecording());
    r.setAudioOnly(dto.isAudioOnly());
    r.setHiddenElements(dto.getHiddenElements());
}

private List<RoomFile> getRoomFiles(Long roomId, List<RoomFileDTO> rfl) {
    List<RoomFile> r = new ArrayList<>();
    if (rfl != null) {
        for (RoomFileDTO rf : rfl) {
            RoomFile f = getRoomFile(rf, roomId);
            if (f.getFile() != null) {
                r.add(f);
            }
        }
    }
    return r;
}

private RoomFile getRoomFile(RoomFileDTO dto, Long roomId) {
    RoomFile f = new RoomFile();
    f.setId(dto.getId());
    f.setRoomId(roomId);
    f.setFile(fileDao.getBase(dto.getFileId()));
    f.setWbIdx(dto.getWbIdx());
    return f;
}

private void setInvitationBasicProperties(Invitation i, InvitationDTO dto) {
    i.setHash(randomUUID().toString());
    i.setPasswordProtected(dto.isPasswordProtected());
    if (dto.isPasswordProtected()) {
        i.setPassword(CryptProvider.get().hash(dto.getPassword()));
    }
    i.setUsed(false);
    i.setValid(dto.getValid());
    i.setDeleted(false);
    i.setInserted(new Date());
    i.setAppointment(null);
}

private void setInvitationValidity(Invitation i, InvitationDTO dto) {
    try {
        switch(dto.getValid()) {
            case PERIOD:
                i.setValidFrom(new Date(SDF.parse(dto.getValidFrom()).getTime() - (5 * 60 * 1000)));
                i.setValidTo(SDF.parse(dto.getValidTo()));
                break;
            case ENDLESS, ONE_TIME:
            default:
                break;
        }
    } catch (ParseException e) {
        log.error("Unexpected error while creating invitation", e);
        throw new RuntimeException(e);
    }
}

private void setInvitationUserDetails(Invitation i, InvitationDTO dto, Long userId) {
    i.setInvitedBy(userDao.get(userId));
    i.setInvitee(userDao.getContact(dto.getEmail(), dto.getFirstname(), dto.getLastname(), userId));
    if (Type.CONTACT == i.getInvitee().getType()) {
        i.getInvitee().setLanguageId(dto.getLanguageId());
    }
}

private void setInvitationRoom(Invitation i, InvitationDTO dto) {
    i.setRoom(roomDao.get(dto.getRoomId()));
}

