@Override
public void delete(MailMessage m, Long userId) {
    delete(m.getId());
}
// ---- helper method(s) introduced by the refactoring ----
private void executeResetSendingStatusQuery(Calendar date, Long id) {
    var query = em.createNamedQuery("resetMailStatusByDate").setParameter("noneStatus", Status.NONE).setParameter("sendingStatus", Status.SENDING);
    if (date != null) {
        query.setParameter("date", date);
    } else if (id != null) {
        query = em.createNamedQuery("resetMailStatusById").setParameter("id", id);
    }
    query.executeUpdate();
}

private MailMessage persist(MailMessage m) {
    em.persist(m);
    return m;
}

private MailMessage merge(MailMessage m) {
    return em.merge(m);
}

