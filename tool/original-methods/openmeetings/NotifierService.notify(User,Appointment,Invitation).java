public void notify(User u, Appointment a, Invitation inv) {
    if (inv == null) {
        log.error("Error retrieving Invitation for member {} in Appointment {}", u.getAddress().getEmail(), a.getTitle());
        return;
    }
    for (INotifier n : notifiers) {
        try {
            n.notify(u, a, inv);
        } catch (Exception e) {
            log.error("Unexpected exception while sending notifications", e);
        }
    }
}