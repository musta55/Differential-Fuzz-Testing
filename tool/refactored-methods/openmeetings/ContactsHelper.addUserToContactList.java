public static Object addUserToContactList(long userIdToAdd) {
    boolean isContact = get().getBean(UserContactDao.class).isContact(userIdToAdd, getUserId());
    if (isContact) {
        return "error.contact.added";
    }
    UserContact contact = get().getBean(UserContactDao.class).add(userIdToAdd, getUserId(), true);
    User user = contact.getOwner();
    User userToAdd = contact.getContact();
    String subj = user.getDisplayName() + " " + Application.getString("1193");
    String message = RequestContactTemplate.getEmail(userToAdd, user);
    get().getBean(PrivateMessageDao.class).addPrivateMessage(subj, message, user, userToAdd, userToAdd, true, contact.getId());
    sendEmailIfAddressExists(userToAdd, subj, message);
    return contact;
}
// ---- helper method(s) introduced by the refactoring ----
private static void sendEmailIfAddressExists(User user, String subject, String message) {
    if (user.getAddress() != null) {
        get().getBean(MailHandler.class).send(user.getAddress().getEmail(), subject, message);
    }
}

