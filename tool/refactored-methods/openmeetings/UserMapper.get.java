public User get(UserDTO dto) {
    User u = getUserFromDto(dto);
    setUserProperties(u, dto);
    handleExternalType(u, dto);
    return u;
}
// ---- helper method(s) introduced by the refactoring ----
private User getUserFromDto(UserDTO dto) {
    return dto.getId() == null ? new User() : userDao.get(dto.getId());
}

private void setUserProperties(User u, UserDTO dto) {
    u.setLogin(dto.getLogin());
    u.setFirstname(dto.getFirstname());
    u.setLastname(dto.getLastname());
    u.setRights(dto.getRights());
    u.setLanguageId(dto.getLanguageId());
    u.setAddress(dto.getAddress());
    u.setTimeZoneId(dto.getTimeZoneId());
    u.setPictureUri(dto.getPictureUri());
}

private void handleExternalType(User u, UserDTO dto) {
    String externalId = dto.getExternalId();
    String externalType = dto.getExternalType();
    Type type = dto.getType();
    if (Type.EXTERNAL == type || (!Strings.isEmpty(externalId) && !Strings.isEmpty(externalType))) {
        type = Type.EXTERNAL;
        if (u.getGroupUsers().stream().noneMatch(gu -> gu.getGroup().isExternal() && gu.getGroup().getName().equals(externalType))) {
            u.addGroup(groupDao.getExternal(externalType));
        }
        u.setExternalId(externalId);
    }
    u.setType(type == null ? Type.USER : type);
}

