public MeetingMember get(MeetingMemberDTO dto, User owner) {
    MeetingMember mm = new MeetingMember();
    mm.setId(dto.getId());
    if (dto.getUser().getId() != null) {
        mm.setUser(userDao.get(dto.getUser().getId()));
    } else {
        User u = null;
        UserDTO user = dto.getUser();
        if (User.Type.EXTERNAL == user.getType()) {
            // try to get ext. user
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
        mm.setUser(u);
    }
    return mm;
}