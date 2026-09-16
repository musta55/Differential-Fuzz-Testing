public Long updateContactStatus(Long id, boolean pending) {
    try {
        UserContact uc = get(id);
        uc.setPending(pending);
        update(uc);
        return id;
    } catch (Exception e) {
        handleException("[updateContactStatus]", e);
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private void handleException(String methodName, Exception e) {
    log.error(methodName, e);
}

