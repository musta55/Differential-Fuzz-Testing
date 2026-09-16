public UserContact add(Long userId, Long ownerId, boolean pending) {
    try {
        UserContact userContact = new UserContact();
        userContact.setInserted(new Date());
        userContact.setOwner(userDao.get(ownerId));
        userContact.setContact(userDao.get(userId));
        userContact.setPending(pending);
        userContact = update(userContact);
        return userContact;
    } catch (Exception e) {
        log.error("[addUserContact]", e);
    }
    return null;
}