@Override
public MailMessage update(MailMessage m, Long userId) {
    if (m.getId() == null) {
        em.persist(m);
    } else {
        m = em.merge(m);
    }
    return m;
}