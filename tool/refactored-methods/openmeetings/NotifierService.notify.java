public void notify(User u, Appointment a, Invitation inv) {
    if (inv == null) {
        handleNotificationError(u, a, "Error retrieving Invitation");
        return;
    }
    for (INotifier n : notifiers) {
        try {
            n.notify(u, a, inv);
        } catch (Exception e) {
            handleNotificationException(e);
        }
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void handleNotificationError(User u, Appointment a, String message) {
    log.error(message + " for member {} in Appointment {}", u.getAddress().getEmail(), a.getTitle());
}

private void handleNotificationException(Exception e) {
    log.error("Unexpected exception while sending notifications", e);
}

