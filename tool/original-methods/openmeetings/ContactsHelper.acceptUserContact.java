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
        get().getBean(MailHandler.class).send(user.getAddress().getEmail(), subj, message);
    }
    return userContactId;
}