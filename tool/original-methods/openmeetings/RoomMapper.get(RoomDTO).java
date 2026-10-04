public Room get(RoomDTO dto) {
    Room r = dto.getId() == null ? new Room() : roomDao.get(dto.getId());
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
    String externalType = dto.getExternalType();
    if (!Strings.isEmpty(externalType) && r.getGroups().stream().filter(gu -> gu.getGroup().isExternal() && gu.getGroup().getName().equals(externalType)).count() == 0) {
        r.addGroup(groupDao.getExternal(externalType));
    }
    r.setRedirectURL(dto.getRedirectUrl());
    r.setModerated(dto.isModerated());
    r.setWaitModerator(dto.isWaitModerator());
    r.setAllowUserQuestions(dto.isAllowUserQuestions());
    r.setAllowRecording(dto.isAllowRecording());
    r.setWaitRecording(dto.isWaitRecording());
    r.setAudioOnly(dto.isAudioOnly());
    r.setHiddenElements(dto.getHiddenElements());
    r.setFiles(get(dto.getId(), dto.getFiles()));
    return r;
}