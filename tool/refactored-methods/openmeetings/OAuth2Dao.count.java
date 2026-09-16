@Override
public long count(String search) {
    return DaoHelper.count(em, OAuthServer.class, search, searchFields, true, null);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isRegistrationAllowedViaOAuth() {
    return isAllowRegisterOauth();
}

private void persistOrMerge(OAuthServer server) {
    if (server.getId() == null) {
        em.persist(server);
    } else {
        em.merge(server);
    }
}

