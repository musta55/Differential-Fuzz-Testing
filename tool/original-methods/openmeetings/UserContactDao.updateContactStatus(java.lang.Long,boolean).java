public Long updateContactStatus(Long id, boolean pending) {
    try {
        UserContact uc = get(id);
        uc.setPending(pending);
        update(uc);
        return id;
    } catch (Exception e) {
        log.error("[updateContactStatus]", e);
    }
    return null;
}