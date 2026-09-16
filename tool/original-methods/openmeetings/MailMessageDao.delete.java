@Override
public void delete(MailMessage m, Long userId) {
    if (m != null) {
        delete(m.getId());
    }
}