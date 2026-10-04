private void sendReminder(User u, Appointment a, Invitation inv) {
    notifierService.notify(u, a, inv);
    if (inv.getHash() != null) {
        invitationDao.update(inv);
    }
}