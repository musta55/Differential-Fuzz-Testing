public static Object acceptUserContact(long userContactId) {
    UserContactDao dao = get().getBean(UserContactDao.class);
    UserContact contact = dao.get(userContactId);
    if (contact == null) {
        return "error.contact.denied";
    }
    if (!contact.isPending()) {
        return "error.contact.approved";
    }
    dao.updateContactStatus(userContactId, false);
    contact = dao.get(userContactId);
    User user = contact.getOwner();
    dao.add(user.getId(), getUserId(), false);
    if (user.getAddress() != null) {
        String message = RequestContactConfirmTemplate.getEmail(contact);
        String subj = contact.getContact().getDisplayName() + " " + Application.getString("1198");
        get().getBean(PrivateMessageDao.class).addPrivateMessage(subj, message, contact.getContact(), user, user, false, 0L);
        sendEmailIfAddressExists(user, subj, message);
    }
    return userContactId;
}
// ---- helper method(s) introduced by the refactoring ----
private static void sendEmailIfAddressExists(User user, String subject, String message) {
    if (user.getAddress() != null) {
        get().getBean(MailHandler.class).send(user.getAddress().getEmail(), subject, message);
    }
}

