// --------------------------------------------------------------------------------------------
private void sendReminder(User u, Appointment a) {
    Invitation i = new Invitation();
    i.setInvitedBy(u);
    i.setInvitee(u);
    i.setAppointment(a);
    i.setRoom(a.getRoom());
    sendReminder(u, a, i);
}